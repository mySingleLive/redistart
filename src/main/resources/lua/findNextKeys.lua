
local startIndex = ARGV[1]
local count = ARGV[2]
local searchInfo = cjson.decode(ARGV[3])
local pattern = searchInfo.pattern
local types = searchInfo.types
local ttlOp = searchInfo.ttlOperator
local ttlCond = searchInfo.ttl

local scanResults = nil

local typeLen = table.getn(types)
local typesMap = {}
for i=1,typeLen do
    local tyName = types[i]
    typesMap[tyName] = true
end

local isSearch = false

if typeLen > 0 then
    isSearch = true
end

if ttlOp ~= nil and ttlOp ~= 'NONE' and ttlCond ~= nil then
    isSearch = true
    ttlCond = tonumber(ttlCond)
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

local ttlCheckTable = {
    EQ = function(ttl)
        return tonumber(ttl) == ttlCond
    end,
    GT = function(ttl)
        return tonumber(ttl) > ttlCond
    end,
    LT = function(ttl)
        ttl = tonumber(ttl)
        return ttl < ttlCond and ttl > -1
    end,
    GE = function(ttl)
        return tonumber(ttl) >= ttlCond
    end,
    LE = function(ttl)
        ttl = tonumber(ttl)
        return tonumber(ttl) <= ttlCond and ttl > -1
    end
}

local checkCondition = function(type, ttl, tyName)
    if typeLen > 0 and typeLen < 5 then
        return typesMap[tyName] ~= nil
    end
    if ttlOp ~= nil and ttlCond ~= nil then
        local func = ttlCheckTable[ttlOp]
        if func == nil then
            return true
        end
        return func(ttl)
    end
    return true
end

local keyData = {}

for i=1,len do
    local key = data[i]
    if key ~= nil then
        local type = redis.call('TYPE', key)
        local ttl = redis.call('TTL', key)
        local tyName = type['ok']
        if checkCondition(type, ttl, tyName) then
            local item = {
                type = tyName,
                ttl = -1,
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
