
local startIndex = ARGV[1]
local count = ARGV[2]
local searchInfo = cjson.decode(ARGV[3])
local pattern = searchInfo.pattern
local isSearch = false

local scanResults = nil

if pattern ~= nil and pattern ~= '' then
    isSearch = true
    scanResults = redis.call('SCAN', startIndex, 'MATCH', pattern, 'COUNT', count)
else
    scanResults = redis.call('SCAN', startIndex, 'COUNT', count)
end

local pos = scanResults[1]
local data = scanResults[2]

local len = table.getn(data)

local keyData = {}

for i=1,len do
    local key = data[i]
    if key ~= nil then
        local type = redis.call('TYPE', key)
        local ttl = redis.call('TTL', key)
        local item = {
            type = type['ok'],
            ttl = ttl,
            key = key
        }
        table.insert(keyData, item)
    end
end

return cjson.encode({
    pos = pos,
    search = isSearch,
    keys = keyData
})