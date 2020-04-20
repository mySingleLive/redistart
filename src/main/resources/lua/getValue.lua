
local key = ARGV[1]
local startIndex = ARGV[2]
local count = ARGV[3]

local result = {
    key = key,
    status = -1,
    type = nil,
    value = nil
}

local typeObj = redis.call('TYPE', key)

if typeObj ~= nil and typeObj['ok'] ~= nil then
    local type = typeObj['ok']
    if type ~= 'none' then
        result.status = 0
        result.type = type
        local ttl = redis.call('TTL', key)
        result.ttl = ttl
        if type == 'string' then
            local val = redis.call('GET', key)
            result.value = val
        end
    end
end

return cjson.encode(result)