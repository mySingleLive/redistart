package com.dtflys.redistart.model.connection;

import com.dtflys.redistart.event.RSEventHandlerList;
import com.dtflys.redistart.model.RedisConnectionConfig;
import com.dtflys.redistart.model.command.RSCommandRecord;
import com.dtflys.redistart.service.CommandService;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.redisson.client.RedisClient;
import org.redisson.client.RedisClientConfig;
import org.redisson.client.protocol.RedisCommands;
import org.redisson.misc.RedisURI;

import java.io.IOException;
import java.net.ServerSocket;


public abstract class BasicRedisConnection extends Conntable {

    protected RedisClient redisClient;

    protected final CommandService commandService;

    protected org.redisson.client.RedisConnection redisConnection;

    protected Session sshSession;

    private RSEventHandlerList<BasicRedisConnection> onOpenConnectionFailed = new RSEventHandlerList<>();

    protected BasicRedisConnection(RedisConnectionConfig connectionConfig, CommandService commandService) {
        super(connectionConfig);
        this.commandService = commandService;
    }

    protected RedisClientConfig createRedisConnectionConfig(int dbIndex) {
        RedisClientConfig config = new RedisClientConfig();
        config.setAddress(connectionConfig.getRedisHost(), connectionConfig.getRedisPort())
                .setClientName(redisClientName(dbIndex))
                .setPassword(connectionConfig.getRedisPassword())
                .setDatabase(dbIndex);
        return config;
    }

    public Session startSSHSession(RedisClientConfig config) throws JSchException, IOException {
        JSch jsch = new JSch();
        //设置ssh目标跳板机连接信息
        ServerSocket socket = new ServerSocket(0);
        String sshHost = connectionConfig.getSshHost();
        int sshPort = connectionConfig.getSshPort();
        String sshUsername = connectionConfig.getSshUsername();
        String privateKeyFile = connectionConfig.getSshPrivateKeyFile();
        int sshLocalPort = socket.getLocalPort();
        if (!socket.isClosed()) {
            socket.close();
        }
        sshSession = jsch.getSession(sshUsername, sshHost, sshPort);
        sshSession.setConfig(
                "PreferredAuthentications",
                "publickey,gssapi-with-mic,keyboard-interactive,password");
        jsch.addIdentity(privateKeyFile);
        sshSession.setConfig("StrictHostKeyChecking", "no");
        //将session绑定本地机器ip和端口
        RedisURI redisURI = config.getAddress();
        sshSession.setPortForwardingL(sshLocalPort, redisURI.getHost(), redisURI.getPort());
        config.setAddress("localhost", sshLocalPort);
        //开启ssh跳板机连接
        sshSession.connect(10000);
        return sshSession;
    }


    @Override
    protected void doOpenConnection(int dbIndex) {
        RedisClientConfig config = createRedisConnectionConfig(dbIndex);
        try {
            startSSHSession(config);
            redisClient = RedisClient.create(config);
            redisConnection = redisClient.connect();
        } catch (JSchException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeConnection() {
        if (sshSession != null && sshSession.isConnected()) {
            sshSession.disconnect();
        }
        if (redisClient != null) {
            try {
                redisClient.shutdown();
            } catch (Throwable th) {
            } finally {
                afterCloseConnection();
            }
        }
    }

    @Override
    public void selectDatabase(int dbIndex) {
        redisClient.getConfig().setDatabase(dbIndex);
        sync(new RSCommandRecord(RedisCommands.SELECT, dbIndex));
    }

    public <T> T sync(RSCommandRecord record) {
        T ret = (T) redisConnection.sync(record.getRedisCommand(), record.getArguments());
        record.doOnResult(ret);
        return ret;
    }


    public RSEventHandlerList<BasicRedisConnection> getOnOpenConnectionFailed() {
        return onOpenConnectionFailed;
    }


    protected String redisClientName(int dbIndex) {
        return connectionConfig.getRedisHost() + ":" + connectionConfig.getRedisPort() + ":" + dbIndex;
    }


    protected abstract void afterCloseConnection();

}
