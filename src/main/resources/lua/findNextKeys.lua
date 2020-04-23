
local startIndex = ARGV[1]
local count = ARGV[2]
local searchInfo = cjson.decode(ARGV[3])
local pattern = searchInfo.pattern
local types = searchInfo.types
local isSearch = false

local scanResults = nil

local typeLen = table.getn(types)
local typesMap = {}
for i=1,typeLen do
    local tyName = types[i]
    typesMap[tyName] = true
end

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
        local tyName = type['ok']
        if typeLen == 0 or typesMap[tyName] ~= nil then
            local item = {
                type = tyName,
                ttl = ttl,
                key = key
            }
            table.insert(keyData, item)
        end
    end
end

return cjson.encode({
    pos = pos,
    search = isSearch,
    keys = keyData
})