# invest-microservice

### Получение всех операций по аккаунту

## `` POST api/v1/invest/operations``

Тело запроса

```bash
{
    "investAccountId": "a7c911bb-5b01-41bf-9db7-3767ac46385d"
}
```

Ответ

1. Успешное получение операций

```bash
[
      {
        "operationId": "05f05e3d-86c4-4c10-af23-a14d00716906",
        "instrumentType": "",
        "assetName": "Российский рубль",
        "figi": "RUB000UTSTOM",
        "currency": "RUB",
        "operationType": "OPERATION_TYPE_INPUT",
        "operationState": "OPERATION_STATE_EXECUTED",
        "quantity": 0,
        "payment": 100.550000000,
        "instrumentPrice": 0,
        "lotPrice": 0,
        "operationDate": "2024-03-14T12:20:04.650845"
    },
        {
        "operationId": "2d0de988-041b-4a31-8be7-8b73621daf8b",
        "instrumentType": "share",
        "assetName": "Polymetal",
        "figi": "BBG004PYF2N3",
        "currency": "rub",
        "operationType": "OPERATION_TYPE_BUY",
        "operationState": "OPERATION_STATE_EXECUTED",
        "quantity": 2,
        "payment": -704.800000000,
        "instrumentPrice": 352.400000000,
        "lotPrice": 352.400000000,
        "operationDate": "2024-03-13T22:33:41.833999"
    },
        {
        "operationId": "875c7a38-49f8-43d1-b6df-c638c970fc34",
        "instrumentType": "share",
        "assetName": "КАМАЗ",
        "figi": "BBG000LNHHJ9",
        "currency": "rub",
        "operationType": "OPERATION_TYPE_BUY",
        "operationState": "OPERATION_STATE_EXECUTED",
        "quantity": 30,
        "payment": -5508.000000000,
        "instrumentPrice": 183.600000000,
        "lotPrice": 1836.000000000,
        "operationDate": "2024-03-14T11:54:44.713586"
    }
]
```

2. Ошибка в получении операций

```bash
{
    "message": "Ошибка получения операций по счёту."
}
```


| Параметр       | Описание                                                           | Тип данных  |
|----------------|--------------------------------------------------------------------|-------------|  
| operationId    | Уникальный идентификатор операции                                  | String      |
| instrmentType  | Название инструмента. Варианты: "share", "bond", "etf", "currency" | String      |
| assetName      | Название актива                                                    | String      |
| figi           | Figi инструмента                                                   | String      |
| currency       | Валюта                                                             | String      |
| operationType  | Вид операции (пополнение, покупка, продажа и тд)                   | String      |
| operationState | Состояние операции (выполнена, активна и тд)                       | String      |
| quantity       | Количество активов в операции                                      | integer     |
| payment        | Сумма операции (+ пополнение, - покупка/вывод)                     | BigDecimal  |
| instrumentPrice | Цена инструмента                                                   | BigDecimal  |
| lotPrice       | Цена за лот                                                        | BigDecimal  |
| operationDate  | Дата совершения операции                                           | String      |

| Код | Описание              |
|-----|-----------------------|
| 200 | ОК                    |
| 400 | Элемент не существует |
