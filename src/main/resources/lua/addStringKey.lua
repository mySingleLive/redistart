
local keyName = ARGV[1]
local value = ARGV[2]

local ret = redis.call('EXISTS', keyName)
if ret == 1 then
    return cjson.encode({
        code = 'KEY_EXIST'
    })
end

redis.call('SET', keyName, value)

return cjson.encode({
   code = 'SUCCESS',
   type = 'string',
   keyName = keyName
})


