# adapter

exported at 2023-08-05 23:56:04

## EnumerationController

EnumerationController


---
### listLabels

> BASIC

**Path:** enumerations/listLabels

**Method:** GET

> REQUEST



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
|  | array |  |
|  | object |  |
| &ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&#124;─desc | string |  |

**Response Demo:**

```json
[
  {
    "code": "",
    "desc": ""
  }
]
```




---
### listProvinces

> BASIC

**Path:** enumerations/listProvinces

**Method:** GET

> REQUEST



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
|  | array |  |
|  | object |  |
| &ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&#124;─desc | string |  |

**Response Demo:**

```json
[
  {
    "code": "",
    "desc": ""
  }
]
```




---
### listUnitTypes

> BASIC

**Path:** enumerations/listUnitTypes

**Method:** GET

> REQUEST



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
|  | array |  |
|  | object |  |
| &ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&#124;─desc | string |  |

**Response Demo:**

```json
[
  {
    "code": "",
    "desc": ""
  }
]
```




---
### listDirections

> BASIC

**Path:** enumerations/listDirections

**Method:** GET

> REQUEST



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
|  | array |  |
|  | object |  |
| &ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&#124;─desc | string |  |

**Response Demo:**

```json
[
  {
    "code": "",
    "desc": ""
  }
]
```




---
### listTimeFrames

> BASIC

**Path:** enumerations/listTimeFrames

**Method:** GET

> REQUEST



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
|  | array |  |
|  | object |  |
| &ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&#124;─desc | string |  |

**Response Demo:**

```json
[
  {
    "code": "",
    "desc": ""
  }
]
```




---
### listMarketStatuses

> BASIC

**Path:** enumerations/listMarketStatuses

**Method:** GET

> REQUEST



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
|  | array |  |
|  | object |  |
| &ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&#124;─desc | string |  |

**Response Demo:**

```json
[
  {
    "code": "",
    "desc": ""
  }
]
```




---
### listMarketTypes

> BASIC

**Path:** enumerations/listMarketTypes

**Method:** GET

> REQUEST



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
|  | array |  |
|  | object |  |
| &ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&#124;─desc | string |  |

**Response Demo:**

```json
[
  {
    "code": "",
    "desc": ""
  }
]
```




---
### listCompStatuses

> BASIC

**Path:** enumerations/listCompStatuses

**Method:** GET

> REQUEST



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
|  | array |  |
|  | object |  |
| &ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&#124;─desc | string |  |

**Response Demo:**

```json
[
  {
    "code": "",
    "desc": ""
  }
]
```





## TestController

TestController


---
### push

> BASIC

**Path:** test/push

**Method:** GET

> REQUEST

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| value |  | NO |  |





## UnitController

UnitController


---
### listUnits

> BASIC

**Path:** unit/listUnits

**Method:** GET

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| token |  | YES |  |

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| compId |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| success | boolean |  |
| data | array |  |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─version | integer |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─unitId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─userId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─compId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─roundId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─metaUnit | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─metaUnitId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─nodeId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─name | string |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─province | string | TRANSFER :TRANSFER<br>RECEIVER :RECEIVER |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─unitType | string | GENERATOR :GENERATOR<br>LOAD :LOAD |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─sourceId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─capacity | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─stageFourDirections | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─balances | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─uniqueIdBuilder | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─tableName | string |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─nameSpace | string |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─queue | array |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─section | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─sectionLoader | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─jdbcTemplate | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─logger | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─dataSource | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─exceptionTranslator | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─lazyInit | boolean |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─ignoreWarnings | boolean |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─fetchSize | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─maxRows | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─queryTimeout | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─skipResultsProcessing | boolean |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─skipUndeclaredResults | boolean |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─resultsMapCaseInsensitive | boolean |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─extractor | object |  |
| code | string |  |
| message | string | the message that can be shown to customer |
| detailMessage | string | the detail message may come from exception |
| exceptionType | string | exception type,{@link ExceptionType} may business exception or system exception<br>BIZ :BIZ<br>SYS :SYS |
| errorEnums | array | on some circumstance, you may need a set of error info |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─message | string |  |
| traceId | string |  |
| date | string |  |
| extendInfo | object | extend info may include some extent info |
| &ensp;&ensp;&#124;─key | object |  |

**Response Demo:**

```json
{
  "success": "true",
  "data": [
    {
      "version": "0",
      "unitId": 0,
      "userId": 0,
      "compId": 0,
      "roundId": 0,
      "metaUnit": {
        "metaUnitId": 0,
        "nodeId": 0,
        "name": "",
        "province": "",
        "unitType": "",
        "sourceId": 0,
        "capacity": {
          "": {
            "": 0.0
          }
        }
      },
      "stageFourDirections": {
        "": ""
      },
      "balances": {
        "": {
          "": 0.0
        }
      },
      "uniqueIdBuilder": {
        "tableName": "",
        "nameSpace": "",
        "queue": [
          {}
        ],
        "section": {},
        "sectionLoader": {
          "jdbcTemplate": {
            "logger": {},
            "dataSource": {},
            "exceptionTranslator": {},
            "lazyInit": false,
            "ignoreWarnings": false,
            "fetchSize": 0,
            "maxRows": 0,
            "queryTimeout": 0,
            "skipResultsProcessing": false,
            "skipUndeclaredResults": false,
            "resultsMapCaseInsensitive": false
          },
          "extractor": {}
        }
      }
    }
  ],
  "code": "",
  "message": "",
  "detailMessage": "",
  "exceptionType": "",
  "errorEnums": [
    {
      "code": "",
      "message": ""
    }
  ],
  "traceId": "",
  "date": "new Date()",
  "extendInfo": {
    "": {}
  }
}
```




---
### listGeneratorDetails

> BASIC

**Path:** unit/listGeneratorDetails

**Method:** GET

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| token |  | YES |  |

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| compId |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| success | boolean |  |
| data | array |  |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─version | integer |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─unitId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─userId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─compId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─roundId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─metaUnit | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─metaUnitId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─nodeId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─name | string |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─province | string | TRANSFER :TRANSFER<br>RECEIVER :RECEIVER |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─unitType | string | GENERATOR :GENERATOR<br>LOAD :LOAD |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─sourceId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─capacity | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─stageFourDirections | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─balances | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─uniqueIdBuilder | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─tableName | string |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─nameSpace | string |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─queue | array |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─section | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─sectionLoader | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─jdbcTemplate | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─logger | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─dataSource | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─exceptionTranslator | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─lazyInit | boolean |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─ignoreWarnings | boolean |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─fetchSize | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─maxRows | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─queryTimeout | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─skipResultsProcessing | boolean |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─skipUndeclaredResults | boolean |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─resultsMapCaseInsensitive | boolean |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─extractor | object |  |
| code | string |  |
| message | string | the message that can be shown to customer |
| detailMessage | string | the detail message may come from exception |
| exceptionType | string | exception type,{@link ExceptionType} may business exception or system exception<br>BIZ :BIZ<br>SYS :SYS |
| errorEnums | array | on some circumstance, you may need a set of error info |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─message | string |  |
| traceId | string |  |
| date | string |  |
| extendInfo | object | extend info may include some extent info |
| &ensp;&ensp;&#124;─key | object |  |

**Response Demo:**

```json
{
  "success": "true",
  "data": [
    {
      "version": "0",
      "unitId": 0,
      "userId": 0,
      "compId": 0,
      "roundId": 0,
      "metaUnit": {
        "metaUnitId": 0,
        "nodeId": 0,
        "name": "",
        "province": "",
        "unitType": "",
        "sourceId": 0,
        "capacity": {
          "": {
            "": 0.0
          }
        }
      },
      "stageFourDirections": {
        "": ""
      },
      "balances": {
        "": {
          "": 0.0
        }
      },
      "uniqueIdBuilder": {
        "tableName": "",
        "nameSpace": "",
        "queue": [
          {}
        ],
        "section": {},
        "sectionLoader": {
          "jdbcTemplate": {
            "logger": {},
            "dataSource": {},
            "exceptionTranslator": {},
            "lazyInit": false,
            "ignoreWarnings": false,
            "fetchSize": 0,
            "maxRows": 0,
            "queryTimeout": 0,
            "skipResultsProcessing": false,
            "skipUndeclaredResults": false,
            "resultsMapCaseInsensitive": false
          },
          "extractor": {}
        }
      }
    }
  ],
  "code": "",
  "message": "",
  "detailMessage": "",
  "exceptionType": "",
  "errorEnums": [
    {
      "code": "",
      "message": ""
    }
  ],
  "traceId": "",
  "date": "new Date()",
  "extendInfo": {
    "": {}
  }
}
```




---
### centralizedBid

> BASIC

**Path:** unit/centralizedBid

**Method:** POST

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| Content-Type | application/json | YES |  |
| token |  | YES |  |

**Request Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| unitId | integer |  |
| bids | array |  |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─timeFrame | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─direction | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─quantity | number |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─price | number |  |

**Request Demo:**

```json
{
  "unitId": 0,
  "bids": [
    {
      "timeFrame": "",
      "direction": "",
      "quantity": 0.0,
      "price": 0.0
    }
  ]
}
```



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| success | boolean |  |
| data | object |  |
| code | string |  |
| message | string | the message that can be shown to customer |
| detailMessage | string | the detail message may come from exception |
| exceptionType | string | exception type,{@link ExceptionType} may business exception or system exception<br>BIZ :BIZ<br>SYS :SYS |
| errorEnums | array | on some circumstance, you may need a set of error info |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─message | string |  |
| traceId | string |  |
| date | string |  |
| extendInfo | object | extend info may include some extent info |
| &ensp;&ensp;&#124;─key | object |  |

**Response Demo:**

```json
{
  "success": "true",
  "data": null,
  "code": "",
  "message": "",
  "detailMessage": "",
  "exceptionType": "",
  "errorEnums": [
    {
      "code": "",
      "message": ""
    }
  ],
  "traceId": "",
  "date": "new Date()",
  "extendInfo": {
    "": {}
  }
}
```




---
### realtimeNewBid

> BASIC

**Path:** unit/realtimeNewBid

**Method:** POST

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| Content-Type | application/json | YES |  |
| token |  | YES |  |

**Request Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| unitId | integer |  |
| bid | object |  |
| &ensp;&ensp;&#124;─timeFrame | string |  |
| &ensp;&ensp;&#124;─direction | string |  |
| &ensp;&ensp;&#124;─quantity | number |  |
| &ensp;&ensp;&#124;─price | number |  |

**Request Demo:**

```json
{
  "unitId": 0,
  "bid": {
    "timeFrame": "",
    "direction": "",
    "quantity": 0.0,
    "price": 0.0
  }
}
```



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| success | boolean |  |
| data | object |  |
| code | string |  |
| message | string | the message that can be shown to customer |
| detailMessage | string | the detail message may come from exception |
| exceptionType | string | exception type,{@link ExceptionType} may business exception or system exception<br>BIZ :BIZ<br>SYS :SYS |
| errorEnums | array | on some circumstance, you may need a set of error info |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─message | string |  |
| traceId | string |  |
| date | string |  |
| extendInfo | object | extend info may include some extent info |
| &ensp;&ensp;&#124;─key | object |  |

**Response Demo:**

```json
{
  "success": "true",
  "data": null,
  "code": "",
  "message": "",
  "detailMessage": "",
  "exceptionType": "",
  "errorEnums": [
    {
      "code": "",
      "message": ""
    }
  ],
  "traceId": "",
  "date": "new Date()",
  "extendInfo": {
    "": {}
  }
}
```




---
### realtimeCancelBid

> BASIC

**Path:** unit/realtimeCancelBid

**Method:** POST

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| Content-Type | application/json | YES |  |
| token |  | YES |  |

**Request Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| unitId | integer |  |
| bidId | integer |  |

**Request Demo:**

```json
{
  "unitId": 0,
  "bidId": 0
}
```



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| success | boolean |  |
| data | object |  |
| code | string |  |
| message | string | the message that can be shown to customer |
| detailMessage | string | the detail message may come from exception |
| exceptionType | string | exception type,{@link ExceptionType} may business exception or system exception<br>BIZ :BIZ<br>SYS :SYS |
| errorEnums | array | on some circumstance, you may need a set of error info |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─message | string |  |
| traceId | string |  |
| date | string |  |
| extendInfo | object | extend info may include some extent info |
| &ensp;&ensp;&#124;─key | object |  |

**Response Demo:**

```json
{
  "success": "true",
  "data": null,
  "code": "",
  "message": "",
  "detailMessage": "",
  "exceptionType": "",
  "errorEnums": [
    {
      "code": "",
      "message": ""
    }
  ],
  "traceId": "",
  "date": "new Date()",
  "extendInfo": {
    "": {}
  }
}
```




---
### listRealtimeBids

> BASIC

**Path:** unit/listRealtimeBids

**Method:** GET

> REQUEST



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| success | boolean |  |
| data | array |  |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─bidId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─unitId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─province | string | TRANSFER :TRANSFER<br>RECEIVER :RECEIVER |
| &ensp;&ensp;&ensp;&ensp;&#124;─timeFrame | string | PEAK :PEAK<br>FLAT :FLAT<br>VALLEY :VALLEY |
| &ensp;&ensp;&ensp;&ensp;&#124;─marketType | string | INTER_ANNUAL_PROVINCIAL :INTER_ANNUAL_PROVINCIAL<br>INTRA_ANNUAL_PROVINCIAL :INTRA_ANNUAL_PROVINCIAL<br>INTER_MONTHLY_PROVINCIAL :INTER_MONTHLY_PROVINCIAL<br>INTRA_MONTHLY_PROVINCIAL :INTRA_MONTHLY_PROVINCIAL<br>INTRA_SPOT_PROVINCIAL :INTRA_SPOT_PROVINCIAL<br>INTER_SPOT_PROVINCIAL :INTER_SPOT_PROVINCIAL<br>FINAL_CLEAR :FINAL_CLEAR |
| &ensp;&ensp;&ensp;&ensp;&#124;─direction | string | BUY :BUY<br>SELL :SELL |
| &ensp;&ensp;&ensp;&ensp;&#124;─quantity | number |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─price | number |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─date | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─deals | array |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─dealId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─bidId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─unitId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─quantity | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─price | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─date | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─bidStatus | string | NEW_DECELERATED :NEW_DECELERATED<br>PART_DEAL :PART_DEAL<br>COMPLETE_DEAL :COMPLETE_DEAL<br>CANCEL_DECELERATED :CANCEL_DECELERATED<br>CANCELLED :CANCELLED |
| code | string |  |
| message | string | the message that can be shown to customer |
| detailMessage | string | the detail message may come from exception |
| exceptionType | string | exception type,{@link ExceptionType} may business exception or system exception<br>BIZ :BIZ<br>SYS :SYS |
| errorEnums | array | on some circumstance, you may need a set of error info |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─message | string |  |
| traceId | string |  |
| date | string |  |
| extendInfo | object | extend info may include some extent info |
| &ensp;&ensp;&#124;─key | object |  |

**Response Demo:**

```json
{
  "success": "true",
  "data": [
    {
      "bidId": 0,
      "unitId": 0,
      "province": "",
      "timeFrame": "",
      "marketType": "",
      "direction": "",
      "quantity": 0.0,
      "price": 0.0,
      "date": "",
      "deals": [
        {
          "dealId": 0,
          "bidId": 0,
          "unitId": 0,
          "quantity": 0.0,
          "price": 0.0,
          "date": ""
        }
      ],
      "bidStatus": ""
    }
  ],
  "code": "",
  "message": "",
  "detailMessage": "",
  "exceptionType": "",
  "errorEnums": [
    {
      "code": "",
      "message": ""
    }
  ],
  "traceId": "",
  "date": "new Date()",
  "extendInfo": {
    "": {}
  }
}
```





## DataController

DataController


---
### 市场公告

> BASIC

**Path:** dataController/marketAnnouncement

**Method:** GET

> REQUEST



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| key | string |  |

**Response Demo:**

```json
{
  "": ""
}
```




---
### systemParameterRelease

> BASIC

**Path:** dataController/systemParameterRelease

**Method:** GET

> REQUEST

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| marketTypeValue |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| key | object |  |
| &ensp;&ensp;&#124;─key | array |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─ | number |  |

**Response Demo:**

```json
{
  "": {
    "": [
      0.0
    ]
  }
}
```




---
### listBlockThermalUnit

> BASIC

**Path:** dataController/listBlockThermalUnit

**Method:** GET

> REQUEST

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| marketTypeValue |  | NO |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| key | object |  |
| &ensp;&ensp;&#124;─histograms | array |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&#124;─lineChart | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─key | array |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─ | number |  |

**Response Demo:**

```json
{
  "": {
    "histograms": [
      {}
    ],
    "lineChart": {
      "": [
        0.0
      ]
    }
  }
}
```




---
### listAgentInventory

> BASIC

**Path:** dataController/listAgentInventory

**Method:** GET

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| token |  | YES |  |

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| marketTypeValue |  | YES |  |
| compId |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| key | object |  |
| &ensp;&ensp;&#124;─key | array |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─ | number |  |

**Response Demo:**

```json
{
  "": {
    "": [
      0.0
    ]
  }
}
```





## UserController

UserController


---
### addUser

> BASIC

**Path:** user/addUser

**Method:** POST

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| Content-Type | application/json | YES |  |
| token |  | YES |  |

**Request Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| role | string |  |
| name | string |  |

**Request Demo:**

```json
{
  "role": "",
  "name": ""
}
```



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| success | boolean |  |
| data | object |  |
| code | string |  |
| message | string | the message that can be shown to customer |
| detailMessage | string | the detail message may come from exception |
| exceptionType | string | exception type,{@link ExceptionType} may business exception or system exception<br>BIZ :BIZ<br>SYS :SYS |
| errorEnums | array | on some circumstance, you may need a set of error info |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─message | string |  |
| traceId | string |  |
| date | string |  |
| extendInfo | object | extend info may include some extent info |
| &ensp;&ensp;&#124;─key | object |  |

**Response Demo:**

```json
{
  "success": "true",
  "data": null,
  "code": "",
  "message": "",
  "detailMessage": "",
  "exceptionType": "",
  "errorEnums": [
    {
      "code": "",
      "message": ""
    }
  ],
  "traceId": "",
  "date": "new Date()",
  "extendInfo": {
    "": {}
  }
}
```




---
### listUsers

> BASIC

**Path:** user/listUsers

**Method:** POST

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| token |  | YES |  |
| Content-Type | application/x-www-form-urlencoded | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| success | boolean |  |
| data | array |  |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─userId | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─name | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─role | string |  |
| code | string |  |
| message | string | the message that can be shown to customer |
| detailMessage | string | the detail message may come from exception |
| exceptionType | string | exception type,{@link ExceptionType} may business exception or system exception<br>BIZ :BIZ<br>SYS :SYS |
| errorEnums | array | on some circumstance, you may need a set of error info |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─message | string |  |
| traceId | string |  |
| date | string |  |
| extendInfo | object | extend info may include some extent info |
| &ensp;&ensp;&#124;─key | object |  |

**Response Demo:**

```json
{
  "success": "true",
  "data": [
    {
      "userId": "",
      "name": "",
      "role": ""
    }
  ],
  "code": "",
  "message": "",
  "detailMessage": "",
  "exceptionType": "",
  "errorEnums": [
    {
      "code": "",
      "message": ""
    }
  ],
  "traceId": "",
  "date": "new Date()",
  "extendInfo": {
    "": {}
  }
}
```




---
### login

> BASIC

**Path:** user/login

**Method:** GET

> REQUEST

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| userId |  | YES |  |
| password |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| success | boolean |  |
| data | object |  |
| &ensp;&ensp;&#124;─token | string |  |
| &ensp;&ensp;&#124;─role | string |  |
| code | string |  |
| message | string | the message that can be shown to customer |
| detailMessage | string | the detail message may come from exception |
| exceptionType | string | exception type,{@link ExceptionType} may business exception or system exception<br>BIZ :BIZ<br>SYS :SYS |
| errorEnums | array | on some circumstance, you may need a set of error info |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─message | string |  |
| traceId | string |  |
| date | string |  |
| extendInfo | object | extend info may include some extent info |
| &ensp;&ensp;&#124;─key | object |  |

**Response Demo:**

```json
{
  "success": "true",
  "data": {
    "token": "",
    "role": ""
  },
  "code": "",
  "message": "",
  "detailMessage": "",
  "exceptionType": "",
  "errorEnums": [
    {
      "code": "",
      "message": ""
    }
  ],
  "traceId": "",
  "date": "new Date()",
  "extendInfo": {
    "": {}
  }
}
```





## CompController

CompController


---
### runningComp

> BASIC

**Path:** comp/runningComp

**Method:** GET

> REQUEST



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| success | boolean |  |
| data | object |  |
| &ensp;&ensp;&#124;─version | integer |  |
| &ensp;&ensp;&#124;─compId | integer |  |
| &ensp;&ensp;&#124;─userTotal | integer |  |
| &ensp;&ensp;&#124;─roundTotal | integer |  |
| &ensp;&ensp;&#124;─roundId | integer |  |
| &ensp;&ensp;&#124;─compStatus | string | INIT :INIT<br>OPEN :OPEN<br>END :END |
| &ensp;&ensp;&#124;─marketType | string | INTER_ANNUAL_PROVINCIAL :INTER_ANNUAL_PROVINCIAL<br>INTRA_ANNUAL_PROVINCIAL :INTRA_ANNUAL_PROVINCIAL<br>INTER_MONTHLY_PROVINCIAL :INTER_MONTHLY_PROVINCIAL<br>INTRA_MONTHLY_PROVINCIAL :INTRA_MONTHLY_PROVINCIAL<br>INTRA_SPOT_PROVINCIAL :INTRA_SPOT_PROVINCIAL<br>INTER_SPOT_PROVINCIAL :INTER_SPOT_PROVINCIAL<br>FINAL_CLEAR :FINAL_CLEAR |
| &ensp;&ensp;&#124;─marketStatus | string | OPEN :OPEN<br>CLOSE :CLOSE |
| &ensp;&ensp;&#124;─priceLimit | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─generatorPriceLimit | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─high | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─low | number |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─loadPriceLimit | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─high | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─low | number |  |
| &ensp;&ensp;&#124;─transLimit | object |  |
| &ensp;&ensp;&#124;─durations | object |  |
| &ensp;&ensp;&#124;─endTime | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─{roundId=0, @required={roundId=false, marketType=false, marketStatus=false}, @comment={roundId=, marketType@options=[{value=INTER_ANNUAL_PROVINCIAL, desc=INTER_ANNUAL_PROVINCIAL}, {value=INTRA_ANNUAL_PROVINCIAL, desc=INTRA_ANNUAL_PROVINCIAL}, {value=INTER_MONTHLY_PROVINCIAL, desc=INTER_MONTHLY_PROVINCIAL}, {value=INTRA_MONTHLY_PROVINCIAL, desc=INTRA_MONTHLY_PROVINCIAL}, {value=INTRA_SPOT_PROVINCIAL, desc=INTRA_SPOT_PROVINCIAL}, {value=INTER_SPOT_PROVINCIAL, desc=INTER_SPOT_PROVINCIAL}, {value=FINAL_CLEAR, desc=FINAL_CLEAR}], marketType=, marketStatus@options=[{value=OPEN, desc=OPEN}, {value=CLOSE, desc=CLOSE}], marketStatus=}, marketType=, marketStatus=} | string |  |
| &ensp;&ensp;&#124;─roundCentralizedDeals | array |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&#124;─rtCompVOMap | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─{roundId=0, @required={roundId=false, marketType=false, province=false, timeFrame=false}, @comment={roundId=, marketType@options=[{value=INTER_ANNUAL_PROVINCIAL, desc=INTER_ANNUAL_PROVINCIAL}, {value=INTRA_ANNUAL_PROVINCIAL, desc=INTRA_ANNUAL_PROVINCIAL}, {value=INTER_MONTHLY_PROVINCIAL, desc=INTER_MONTHLY_PROVINCIAL}, {value=INTRA_MONTHLY_PROVINCIAL, desc=INTRA_MONTHLY_PROVINCIAL}, {value=INTRA_SPOT_PROVINCIAL, desc=INTRA_SPOT_PROVINCIAL}, {value=INTER_SPOT_PROVINCIAL, desc=INTER_SPOT_PROVINCIAL}, {value=FINAL_CLEAR, desc=FINAL_CLEAR}], marketType=, province@options=[{value=TRANSFER, desc=TRANSFER}, {value=RECEIVER, desc=RECEIVER}], province=, timeFrame@options=[{value=PEAK, desc=PEAK}, {value=FLAT, desc=FLAT}, {value=VALLEY, desc=VALLEY}], timeFrame=}, marketType=, province=, timeFrame=} | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─currentPrice | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─buyMarketAsks | array |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─price | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─quantity | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─sellMarketAsks | array |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─price | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─quantity | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─deals | array |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─dealId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─bidId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─unitId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─quantity | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─price | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─date | string |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─buyBids | array |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─bidId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─unitId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─province | string | TRANSFER :TRANSFER<br>RECEIVER :RECEIVER |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─timeFrame | string | PEAK :PEAK<br>FLAT :FLAT<br>VALLEY :VALLEY |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─marketType | string | INTER_ANNUAL_PROVINCIAL :INTER_ANNUAL_PROVINCIAL<br>INTRA_ANNUAL_PROVINCIAL :INTRA_ANNUAL_PROVINCIAL<br>INTER_MONTHLY_PROVINCIAL :INTER_MONTHLY_PROVINCIAL<br>INTRA_MONTHLY_PROVINCIAL :INTRA_MONTHLY_PROVINCIAL<br>INTRA_SPOT_PROVINCIAL :INTRA_SPOT_PROVINCIAL<br>INTER_SPOT_PROVINCIAL :INTER_SPOT_PROVINCIAL<br>FINAL_CLEAR :FINAL_CLEAR |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─direction | string | BUY :BUY<br>SELL :SELL |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─quantity | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─price | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─date | string |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─sellBids | array |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─bidId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─unitId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─province | string | TRANSFER :TRANSFER<br>RECEIVER :RECEIVER |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─timeFrame | string | PEAK :PEAK<br>FLAT :FLAT<br>VALLEY :VALLEY |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─marketType | string | INTER_ANNUAL_PROVINCIAL :INTER_ANNUAL_PROVINCIAL<br>INTRA_ANNUAL_PROVINCIAL :INTRA_ANNUAL_PROVINCIAL<br>INTER_MONTHLY_PROVINCIAL :INTER_MONTHLY_PROVINCIAL<br>INTRA_MONTHLY_PROVINCIAL :INTRA_MONTHLY_PROVINCIAL<br>INTRA_SPOT_PROVINCIAL :INTRA_SPOT_PROVINCIAL<br>INTER_SPOT_PROVINCIAL :INTER_SPOT_PROVINCIAL<br>FINAL_CLEAR :FINAL_CLEAR |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─direction | string | BUY :BUY<br>SELL :SELL |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─quantity | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─price | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─date | string |  |
| code | string |  |
| message | string | the message that can be shown to customer |
| detailMessage | string | the detail message may come from exception |
| exceptionType | string | exception type,{@link ExceptionType} may business exception or system exception<br>BIZ :BIZ<br>SYS :SYS |
| errorEnums | array | on some circumstance, you may need a set of error info |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─message | string |  |
| traceId | string |  |
| date | string |  |
| extendInfo | object | extend info may include some extent info |
| &ensp;&ensp;&#124;─key | object |  |

**Response Demo:**

```json
{
  "success": "true",
  "data": {
    "version": "0",
    "compId": 0,
    "userTotal": 0,
    "roundTotal": 0,
    "roundId": 0,
    "compStatus": "",
    "marketType": "",
    "marketStatus": "",
    "priceLimit": {
      "generatorPriceLimit": {
        "high": 0.0,
        "low": 0.0
      },
      "loadPriceLimit": {
        "high": 0.0,
        "low": 0.0
      }
    },
    "transLimit": {
      "": {
        "": {
          "high": 0.0,
          "low": 0.0
        }
      }
    },
    "durations": {
      "": {
        "": {
          "seconds": 0,
          "nanos": 0
        }
      }
    },
    "endTime": {
      "{roundId=0, @required={roundId=false, marketType=false, marketStatus=false}, @comment={roundId=, marketType@options=[{value=INTER_ANNUAL_PROVINCIAL, desc=INTER_ANNUAL_PROVINCIAL}, {value=INTRA_ANNUAL_PROVINCIAL, desc=INTRA_ANNUAL_PROVINCIAL}, {value=INTER_MONTHLY_PROVINCIAL, desc=INTER_MONTHLY_PROVINCIAL}, {value=INTRA_MONTHLY_PROVINCIAL, desc=INTRA_MONTHLY_PROVINCIAL}, {value=INTRA_SPOT_PROVINCIAL, desc=INTRA_SPOT_PROVINCIAL}, {value=INTER_SPOT_PROVINCIAL, desc=INTER_SPOT_PROVINCIAL}, {value=FINAL_CLEAR, desc=FINAL_CLEAR}], marketType=, marketStatus@options=[{value=OPEN, desc=OPEN}, {value=CLOSE, desc=CLOSE}], marketStatus=}, marketType=, marketStatus=}": ""
    },
    "roundCentralizedDeals": [
      {
        "": {
          "": {
            "buyBidQuantityTotal": 0.0,
            "sellBidQuantityTotal": 0.0,
            "dealQuantityTotal": 0.0,
            "dealAveragePrice": 0.0,
            "buyPointLines": [
              {
                "bidId": 0,
                "unitId": 0,
                "direction": "",
                "quantity": 0.0,
                "price": 0.0,
                "leftX": 0.0,
                "rightX": 0.0,
                "width": 0.0,
                "y": 0.0
              }
            ],
            "sellPointLines": [
              {
                "bidId": 0,
                "unitId": 0,
                "direction": "",
                "quantity": 0.0,
                "price": 0.0,
                "leftX": 0.0,
                "rightX": 0.0,
                "width": 0.0,
                "y": 0.0
              }
            ],
            "interPoint": {},
            "deals": [
              {
                "dealId": 0,
                "bidId": 0,
                "unitId": 0,
                "quantity": 0.0,
                "price": 0.0,
                "date": ""
              }
            ]
          }
        }
      }
    ],
    "rtCompVOMap": {
      "{roundId=0, @required={roundId=false, marketType=false, province=false, timeFrame=false}, @comment={roundId=, marketType@options=[{value=INTER_ANNUAL_PROVINCIAL, desc=INTER_ANNUAL_PROVINCIAL}, {value=INTRA_ANNUAL_PROVINCIAL, desc=INTRA_ANNUAL_PROVINCIAL}, {value=INTER_MONTHLY_PROVINCIAL, desc=INTER_MONTHLY_PROVINCIAL}, {value=INTRA_MONTHLY_PROVINCIAL, desc=INTRA_MONTHLY_PROVINCIAL}, {value=INTRA_SPOT_PROVINCIAL, desc=INTRA_SPOT_PROVINCIAL}, {value=INTER_SPOT_PROVINCIAL, desc=INTER_SPOT_PROVINCIAL}, {value=FINAL_CLEAR, desc=FINAL_CLEAR}], marketType=, province@options=[{value=TRANSFER, desc=TRANSFER}, {value=RECEIVER, desc=RECEIVER}], province=, timeFrame@options=[{value=PEAK, desc=PEAK}, {value=FLAT, desc=FLAT}, {value=VALLEY, desc=VALLEY}], timeFrame=}, marketType=, province=, timeFrame=}": {
        "currentPrice": 0.0,
        "buyMarketAsks": [
          {
            "price": 0.0,
            "quantity": 0.0
          }
        ],
        "sellMarketAsks": [
          {
            "price": 0.0,
            "quantity": 0.0
          }
        ],
        "deals": [
          {
            "dealId": 0,
            "bidId": 0,
            "unitId": 0,
            "quantity": 0.0,
            "price": 0.0,
            "date": ""
          }
        ],
        "buyBids": [
          {
            "bidId": 0,
            "unitId": 0,
            "province": "",
            "timeFrame": "",
            "marketType": "",
            "direction": "",
            "quantity": 0.0,
            "price": 0.0,
            "date": ""
          }
        ],
        "sellBids": [
          {
            "bidId": 0,
            "unitId": 0,
            "province": "",
            "timeFrame": "",
            "marketType": "",
            "direction": "",
            "quantity": 0.0,
            "price": 0.0,
            "date": ""
          }
        ]
      }
    }
  },
  "code": "",
  "message": "",
  "detailMessage": "",
  "exceptionType": "",
  "errorEnums": [
    {
      "code": "",
      "message": ""
    }
  ],
  "traceId": "",
  "date": "new Date()",
  "extendInfo": {
    "": {}
  }
}
```




---
### listComps

> BASIC

**Path:** comp/listComps

**Method:** GET

> REQUEST



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| success | boolean |  |
| data | array |  |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─version | integer |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─compId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─userTotal | integer |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─roundTotal | integer |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─roundId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─compStatus | string | INIT :INIT<br>OPEN :OPEN<br>END :END |
| &ensp;&ensp;&ensp;&ensp;&#124;─marketType | string | INTER_ANNUAL_PROVINCIAL :INTER_ANNUAL_PROVINCIAL<br>INTRA_ANNUAL_PROVINCIAL :INTRA_ANNUAL_PROVINCIAL<br>INTER_MONTHLY_PROVINCIAL :INTER_MONTHLY_PROVINCIAL<br>INTRA_MONTHLY_PROVINCIAL :INTRA_MONTHLY_PROVINCIAL<br>INTRA_SPOT_PROVINCIAL :INTRA_SPOT_PROVINCIAL<br>INTER_SPOT_PROVINCIAL :INTER_SPOT_PROVINCIAL<br>FINAL_CLEAR :FINAL_CLEAR |
| &ensp;&ensp;&ensp;&ensp;&#124;─marketStatus | string | OPEN :OPEN<br>CLOSE :CLOSE |
| &ensp;&ensp;&ensp;&ensp;&#124;─priceLimit | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─generatorPriceLimit | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─high | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─low | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─loadPriceLimit | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─high | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─low | number |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─transLimit | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─durations | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─endTime | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─{roundId=0, @required={roundId=false, marketType=false, marketStatus=false}, @comment={roundId=, marketType@options=[{value=INTER_ANNUAL_PROVINCIAL, desc=INTER_ANNUAL_PROVINCIAL}, {value=INTRA_ANNUAL_PROVINCIAL, desc=INTRA_ANNUAL_PROVINCIAL}, {value=INTER_MONTHLY_PROVINCIAL, desc=INTER_MONTHLY_PROVINCIAL}, {value=INTRA_MONTHLY_PROVINCIAL, desc=INTRA_MONTHLY_PROVINCIAL}, {value=INTRA_SPOT_PROVINCIAL, desc=INTRA_SPOT_PROVINCIAL}, {value=INTER_SPOT_PROVINCIAL, desc=INTER_SPOT_PROVINCIAL}, {value=FINAL_CLEAR, desc=FINAL_CLEAR}], marketType=, marketStatus@options=[{value=OPEN, desc=OPEN}, {value=CLOSE, desc=CLOSE}], marketStatus=}, marketType=, marketStatus=} | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─roundCentralizedDeals | array |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─rtCompVOMap | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─{roundId=0, @required={roundId=false, marketType=false, province=false, timeFrame=false}, @comment={roundId=, marketType@options=[{value=INTER_ANNUAL_PROVINCIAL, desc=INTER_ANNUAL_PROVINCIAL}, {value=INTRA_ANNUAL_PROVINCIAL, desc=INTRA_ANNUAL_PROVINCIAL}, {value=INTER_MONTHLY_PROVINCIAL, desc=INTER_MONTHLY_PROVINCIAL}, {value=INTRA_MONTHLY_PROVINCIAL, desc=INTRA_MONTHLY_PROVINCIAL}, {value=INTRA_SPOT_PROVINCIAL, desc=INTRA_SPOT_PROVINCIAL}, {value=INTER_SPOT_PROVINCIAL, desc=INTER_SPOT_PROVINCIAL}, {value=FINAL_CLEAR, desc=FINAL_CLEAR}], marketType=, province@options=[{value=TRANSFER, desc=TRANSFER}, {value=RECEIVER, desc=RECEIVER}], province=, timeFrame@options=[{value=PEAK, desc=PEAK}, {value=FLAT, desc=FLAT}, {value=VALLEY, desc=VALLEY}], timeFrame=}, marketType=, province=, timeFrame=} | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─currentPrice | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─buyMarketAsks | array |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─price | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─quantity | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─sellMarketAsks | array |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─price | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─quantity | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─deals | array |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─dealId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─bidId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─unitId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─quantity | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─price | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─date | string |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─buyBids | array |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─bidId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─unitId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─province | string | TRANSFER :TRANSFER<br>RECEIVER :RECEIVER |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─timeFrame | string | PEAK :PEAK<br>FLAT :FLAT<br>VALLEY :VALLEY |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─marketType | string | INTER_ANNUAL_PROVINCIAL :INTER_ANNUAL_PROVINCIAL<br>INTRA_ANNUAL_PROVINCIAL :INTRA_ANNUAL_PROVINCIAL<br>INTER_MONTHLY_PROVINCIAL :INTER_MONTHLY_PROVINCIAL<br>INTRA_MONTHLY_PROVINCIAL :INTRA_MONTHLY_PROVINCIAL<br>INTRA_SPOT_PROVINCIAL :INTRA_SPOT_PROVINCIAL<br>INTER_SPOT_PROVINCIAL :INTER_SPOT_PROVINCIAL<br>FINAL_CLEAR :FINAL_CLEAR |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─direction | string | BUY :BUY<br>SELL :SELL |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─quantity | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─price | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─date | string |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─sellBids | array |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─bidId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─unitId | integer |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─province | string | TRANSFER :TRANSFER<br>RECEIVER :RECEIVER |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─timeFrame | string | PEAK :PEAK<br>FLAT :FLAT<br>VALLEY :VALLEY |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─marketType | string | INTER_ANNUAL_PROVINCIAL :INTER_ANNUAL_PROVINCIAL<br>INTRA_ANNUAL_PROVINCIAL :INTRA_ANNUAL_PROVINCIAL<br>INTER_MONTHLY_PROVINCIAL :INTER_MONTHLY_PROVINCIAL<br>INTRA_MONTHLY_PROVINCIAL :INTRA_MONTHLY_PROVINCIAL<br>INTRA_SPOT_PROVINCIAL :INTRA_SPOT_PROVINCIAL<br>INTER_SPOT_PROVINCIAL :INTER_SPOT_PROVINCIAL<br>FINAL_CLEAR :FINAL_CLEAR |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─direction | string | BUY :BUY<br>SELL :SELL |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─quantity | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─price | number |  |
| &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&#124;─date | string |  |
| code | string |  |
| message | string | the message that can be shown to customer |
| detailMessage | string | the detail message may come from exception |
| exceptionType | string | exception type,{@link ExceptionType} may business exception or system exception<br>BIZ :BIZ<br>SYS :SYS |
| errorEnums | array | on some circumstance, you may need a set of error info |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─message | string |  |
| traceId | string |  |
| date | string |  |
| extendInfo | object | extend info may include some extent info |
| &ensp;&ensp;&#124;─key | object |  |

**Response Demo:**

```json
{
  "success": "true",
  "data": [
    {
      "version": "0",
      "compId": 0,
      "userTotal": 0,
      "roundTotal": 0,
      "roundId": 0,
      "compStatus": "",
      "marketType": "",
      "marketStatus": "",
      "priceLimit": {
        "generatorPriceLimit": {
          "high": 0.0,
          "low": 0.0
        },
        "loadPriceLimit": {
          "high": 0.0,
          "low": 0.0
        }
      },
      "transLimit": {
        "": {
          "": {
            "high": 0.0,
            "low": 0.0
          }
        }
      },
      "durations": {
        "": {
          "": {
            "seconds": 0,
            "nanos": 0
          }
        }
      },
      "endTime": {
        "{roundId=0, @required={roundId=false, marketType=false, marketStatus=false}, @comment={roundId=, marketType@options=[{value=INTER_ANNUAL_PROVINCIAL, desc=INTER_ANNUAL_PROVINCIAL}, {value=INTRA_ANNUAL_PROVINCIAL, desc=INTRA_ANNUAL_PROVINCIAL}, {value=INTER_MONTHLY_PROVINCIAL, desc=INTER_MONTHLY_PROVINCIAL}, {value=INTRA_MONTHLY_PROVINCIAL, desc=INTRA_MONTHLY_PROVINCIAL}, {value=INTRA_SPOT_PROVINCIAL, desc=INTRA_SPOT_PROVINCIAL}, {value=INTER_SPOT_PROVINCIAL, desc=INTER_SPOT_PROVINCIAL}, {value=FINAL_CLEAR, desc=FINAL_CLEAR}], marketType=, marketStatus@options=[{value=OPEN, desc=OPEN}, {value=CLOSE, desc=CLOSE}], marketStatus=}, marketType=, marketStatus=}": ""
      },
      "roundCentralizedDeals": [
        {
          "": {
            "": {
              "buyBidQuantityTotal": 0.0,
              "sellBidQuantityTotal": 0.0,
              "dealQuantityTotal": 0.0,
              "dealAveragePrice": 0.0,
              "buyPointLines": [
                {
                  "bidId": 0,
                  "unitId": 0,
                  "direction": "",
                  "quantity": 0.0,
                  "price": 0.0,
                  "leftX": 0.0,
                  "rightX": 0.0,
                  "width": 0.0,
                  "y": 0.0
                }
              ],
              "sellPointLines": [
                {
                  "bidId": 0,
                  "unitId": 0,
                  "direction": "",
                  "quantity": 0.0,
                  "price": 0.0,
                  "leftX": 0.0,
                  "rightX": 0.0,
                  "width": 0.0,
                  "y": 0.0
                }
              ],
              "interPoint": {},
              "deals": [
                {
                  "dealId": 0,
                  "bidId": 0,
                  "unitId": 0,
                  "quantity": 0.0,
                  "price": 0.0,
                  "date": ""
                }
              ]
            }
          }
        }
      ],
      "rtCompVOMap": {
        "{roundId=0, @required={roundId=false, marketType=false, province=false, timeFrame=false}, @comment={roundId=, marketType@options=[{value=INTER_ANNUAL_PROVINCIAL, desc=INTER_ANNUAL_PROVINCIAL}, {value=INTRA_ANNUAL_PROVINCIAL, desc=INTRA_ANNUAL_PROVINCIAL}, {value=INTER_MONTHLY_PROVINCIAL, desc=INTER_MONTHLY_PROVINCIAL}, {value=INTRA_MONTHLY_PROVINCIAL, desc=INTRA_MONTHLY_PROVINCIAL}, {value=INTRA_SPOT_PROVINCIAL, desc=INTRA_SPOT_PROVINCIAL}, {value=INTER_SPOT_PROVINCIAL, desc=INTER_SPOT_PROVINCIAL}, {value=FINAL_CLEAR, desc=FINAL_CLEAR}], marketType=, province@options=[{value=TRANSFER, desc=TRANSFER}, {value=RECEIVER, desc=RECEIVER}], province=, timeFrame@options=[{value=PEAK, desc=PEAK}, {value=FLAT, desc=FLAT}, {value=VALLEY, desc=VALLEY}], timeFrame=}, marketType=, province=, timeFrame=}": {
          "currentPrice": 0.0,
          "buyMarketAsks": [
            {
              "price": 0.0,
              "quantity": 0.0
            }
          ],
          "sellMarketAsks": [
            {
              "price": 0.0,
              "quantity": 0.0
            }
          ],
          "deals": [
            {
              "dealId": 0,
              "bidId": 0,
              "unitId": 0,
              "quantity": 0.0,
              "price": 0.0,
              "date": ""
            }
          ],
          "buyBids": [
            {
              "bidId": 0,
              "unitId": 0,
              "province": "",
              "timeFrame": "",
              "marketType": "",
              "direction": "",
              "quantity": 0.0,
              "price": 0.0,
              "date": ""
            }
          ],
          "sellBids": [
            {
              "bidId": 0,
              "unitId": 0,
              "province": "",
              "timeFrame": "",
              "marketType": "",
              "direction": "",
              "quantity": 0.0,
              "price": 0.0,
              "date": ""
            }
          ]
        }
      }
    }
  ],
  "code": "",
  "message": "",
  "detailMessage": "",
  "exceptionType": "",
  "errorEnums": [
    {
      "code": "",
      "message": ""
    }
  ],
  "traceId": "",
  "date": "new Date()",
  "extendInfo": {
    "": {}
  }
}
```




---
### getDurations

> BASIC

**Path:** comp/getDurations

**Method:** GET

> REQUEST



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| success | boolean |  |
| data | object |  |
| code | string |  |
| message | string | the message that can be shown to customer |
| detailMessage | string | the detail message may come from exception |
| exceptionType | string | exception type,{@link ExceptionType} may business exception or system exception<br>BIZ :BIZ<br>SYS :SYS |
| errorEnums | array | on some circumstance, you may need a set of error info |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─message | string |  |
| traceId | string |  |
| date | string |  |
| extendInfo | object | extend info may include some extent info |
| &ensp;&ensp;&#124;─key | object |  |

**Response Demo:**

```json
{
  "success": "true",
  "data": {
    "": {
      "": 0
    }
  },
  "code": "",
  "message": "",
  "detailMessage": "",
  "exceptionType": "",
  "errorEnums": [
    {
      "code": "",
      "message": ""
    }
  ],
  "traceId": "",
  "date": "new Date()",
  "extendInfo": {
    "": {}
  }
}
```




---
### create

> BASIC

**Path:** comp/create

**Method:** POST

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| token |  | YES |  |
| Content-Type | application/json | YES |  |

**Request Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| agentNumber | integer |  |
| durations | object |  |
| &ensp;&ensp;&#124;─key | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─key | integer |  |

**Request Demo:**

```json
{
  "agentNumber": 0,
  "durations": {
    "": {
      "": 0
    }
  }
}
```



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| success | boolean |  |
| data | object |  |
| code | string |  |
| message | string | the message that can be shown to customer |
| detailMessage | string | the detail message may come from exception |
| exceptionType | string | exception type,{@link ExceptionType} may business exception or system exception<br>BIZ :BIZ<br>SYS :SYS |
| errorEnums | array | on some circumstance, you may need a set of error info |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─message | string |  |
| traceId | string |  |
| date | string |  |
| extendInfo | object | extend info may include some extent info |
| &ensp;&ensp;&#124;─key | object |  |

**Response Demo:**

```json
{
  "success": "true",
  "data": null,
  "code": "",
  "message": "",
  "detailMessage": "",
  "exceptionType": "",
  "errorEnums": [
    {
      "code": "",
      "message": ""
    }
  ],
  "traceId": "",
  "date": "new Date()",
  "extendInfo": {
    "": {}
  }
}
```




---
### start

> BASIC

**Path:** comp/start

**Method:** POST

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| token |  | YES |  |
| Content-Type | application/x-www-form-urlencoded | YES |  |

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| compId |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| success | boolean |  |
| data | object |  |
| code | string |  |
| message | string | the message that can be shown to customer |
| detailMessage | string | the detail message may come from exception |
| exceptionType | string | exception type,{@link ExceptionType} may business exception or system exception<br>BIZ :BIZ<br>SYS :SYS |
| errorEnums | array | on some circumstance, you may need a set of error info |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─message | string |  |
| traceId | string |  |
| date | string |  |
| extendInfo | object | extend info may include some extent info |
| &ensp;&ensp;&#124;─key | object |  |

**Response Demo:**

```json
{
  "success": "true",
  "data": null,
  "code": "",
  "message": "",
  "detailMessage": "",
  "exceptionType": "",
  "errorEnums": [
    {
      "code": "",
      "message": ""
    }
  ],
  "traceId": "",
  "date": "new Date()",
  "extendInfo": {
    "": {}
  }
}
```




---
### closeAll

> BASIC

**Path:** comp/closeAll

**Method:** POST

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| token |  | YES |  |
| Content-Type | application/x-www-form-urlencoded | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| success | boolean |  |
| data | object |  |
| code | string |  |
| message | string | the message that can be shown to customer |
| detailMessage | string | the detail message may come from exception |
| exceptionType | string | exception type,{@link ExceptionType} may business exception or system exception<br>BIZ :BIZ<br>SYS :SYS |
| errorEnums | array | on some circumstance, you may need a set of error info |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─message | string |  |
| traceId | string |  |
| date | string |  |
| extendInfo | object | extend info may include some extent info |
| &ensp;&ensp;&#124;─key | object |  |

**Response Demo:**

```json
{
  "success": "true",
  "data": null,
  "code": "",
  "message": "",
  "detailMessage": "",
  "exceptionType": "",
  "errorEnums": [
    {
      "code": "",
      "message": ""
    }
  ],
  "traceId": "",
  "date": "new Date()",
  "extendInfo": {
    "": {}
  }
}
```




---
### edit

> BASIC

**Path:** comp/edit

**Method:** POST

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| token |  | YES |  |
| Content-Type | application/json | YES |  |

**Request Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| compId | integer |  |
| durations | array |  |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─key | integer |  |

**Request Demo:**

```json
{
  "compId": 0,
  "durations": [
    {
      "": 0
    }
  ]
}
```



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| success | boolean |  |
| data | object |  |
| code | string |  |
| message | string | the message that can be shown to customer |
| detailMessage | string | the detail message may come from exception |
| exceptionType | string | exception type,{@link ExceptionType} may business exception or system exception<br>BIZ :BIZ<br>SYS :SYS |
| errorEnums | array | on some circumstance, you may need a set of error info |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─message | string |  |
| traceId | string |  |
| date | string |  |
| extendInfo | object | extend info may include some extent info |
| &ensp;&ensp;&#124;─key | object |  |

**Response Demo:**

```json
{
  "success": "true",
  "data": null,
  "code": "",
  "message": "",
  "detailMessage": "",
  "exceptionType": "",
  "errorEnums": [
    {
      "code": "",
      "message": ""
    }
  ],
  "traceId": "",
  "date": "new Date()",
  "extendInfo": {
    "": {}
  }
}
```




---
### step

> BASIC

**Path:** comp/step

**Method:** POST

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| token |  | YES |  |
| Content-Type | application/x-www-form-urlencoded | YES |  |

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| compId |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| success | boolean |  |
| data | object |  |
| code | string |  |
| message | string | the message that can be shown to customer |
| detailMessage | string | the detail message may come from exception |
| exceptionType | string | exception type,{@link ExceptionType} may business exception or system exception<br>BIZ :BIZ<br>SYS :SYS |
| errorEnums | array | on some circumstance, you may need a set of error info |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─message | string |  |
| traceId | string |  |
| date | string |  |
| extendInfo | object | extend info may include some extent info |
| &ensp;&ensp;&#124;─key | object |  |

**Response Demo:**

```json
{
  "success": "true",
  "data": null,
  "code": "",
  "message": "",
  "detailMessage": "",
  "exceptionType": "",
  "errorEnums": [
    {
      "code": "",
      "message": ""
    }
  ],
  "traceId": "",
  "date": "new Date()",
  "extendInfo": {
    "": {}
  }
}
```




---
### close

> BASIC

**Path:** comp/close

**Method:** POST

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| token |  | YES |  |
| Content-Type | application/x-www-form-urlencoded | YES |  |

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| compId |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| success | boolean |  |
| data | object |  |
| code | string |  |
| message | string | the message that can be shown to customer |
| detailMessage | string | the detail message may come from exception |
| exceptionType | string | exception type,{@link ExceptionType} may business exception or system exception<br>BIZ :BIZ<br>SYS :SYS |
| errorEnums | array | on some circumstance, you may need a set of error info |
| &ensp;&ensp;&#124;─ | object |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─code | string |  |
| &ensp;&ensp;&ensp;&ensp;&#124;─message | string |  |
| traceId | string |  |
| date | string |  |
| extendInfo | object | extend info may include some extent info |
| &ensp;&ensp;&#124;─key | object |  |

**Response Demo:**

```json
{
  "success": "true",
  "data": null,
  "code": "",
  "message": "",
  "detailMessage": "",
  "exceptionType": "",
  "errorEnums": [
    {
      "code": "",
      "message": ""
    }
  ],
  "traceId": "",
  "date": "new Date()",
  "extendInfo": {
    "": {}
  }
}
```





