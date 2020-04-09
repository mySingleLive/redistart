
local startIndex = ARGV[1]
local count = ARGV[2]

local scanResults = redis.call('SCAN', startIndex, 'COUNT', count)

local pos = scanResults[1]
local data = scanResults[2]

local len = table.getn(data)

local keyData = {}

for i=1,len do
    local key = data[i]
    local type = redis.call('TYPE', key)
    local ttl = redis.call('TTL', key)
    local item = {
        type = type['ok'],
        ttl = ttl,
        key = key
    }
    table.insert(keyData, item)
end

return cjson.encode({
    pos = pos,
    keys = keyData
})