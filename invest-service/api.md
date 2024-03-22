# invest-microservice

### Получение всех операций по аккаунту

## `` POST api/v1/invest/operations``

### Тело запроса

```bash
{
    "investAccountId": "a7c911bb-5b01-41bf-9db7-3767ac46385d"
}
```

### Ответ

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

---

## `` POST api/v1/invest/stocks/buy``

### Тело запроса

```bash
{
    "accountId": "a7c911bb-5b01-41bf-9db7-3767ac46385d",
    "figi": "TCS00A106YF0",
    "lot": 1,
    "orderType": "ORDER_TYPE_BESTPRICE"
}
```

### Ответ

1. Успешная покупка акции

```bash
{
    "orderId": "7781fc18-d501-4949-8537-a864dff46e7b",
    "executionStatus": "EXECUTION_REPORT_STATUS_FILL",
    "lotRequested": 1,
    "lotExecuted": 1,
    "asset": {
        "ticker": "VKCO",
        "figi": "TCS00A106YF0",
        "name": "ВК",
        "sector": "it",
        "currency": "rub",
        "countryOfRiskName": "Российская Федерация",
        "lots": 1
    },
    "initialOrderPrice": 637.600000000,
    "executedOrderPrice": 637.600000000,
    "totalOrderPrice": 637.600000000
}
```

2. Ошибка при невалидном accountId из запроса

```bash
{
    "message": "Счёт не найден по переданному account_id.Укажите корректный account_id."
}
```

3. Ошибка при невалидном figi из запроса

```bash
{
    "message": "Инструмент не найден.Укажите корректный идентификатор инструмента."
}
```

### Поля запроса/ответа

1. Запрос

| Параметр  | Описание                                           | Тип данных |
|-----------|----------------------------------------------------|------------|  
| accountId | Уникальный идентификатор аккаунта пользователя     | String     |
| figi      | Figi инструмента                                   | String     |
| lot       | Колличество лотов для покупки                      | integer    |
| orderType | Вид заявки (по лучшей цене, по рыночной, лимитная) | String     |

2. Ответ

| Параметр          | Описание                             | Тип данных |
|-------------------|--------------------------------------|------------|  
| orderId           | Уникальный идентификатор заявки      | String     |
| excecutionStatus  | Текущий статус заявки                | String     |
| assetName         | Название актива                      | String     |
| lotRequested      | Запрошено лотов на покупку           | integer    |
| lotExecuted       | Колличество исполненных лотов        | integer    |
| asset             | Информация об инструменте (акции)    | object     |
| ticker            | Тикер акции                          | String     |
| figi              | Фиги акции                           | String     |
| name              | Название акции                       | String     |
| sector            | Сектор акции (энергетика, it и тд)   | String     |
| currency          | Валюта акции                         | String     |
| countryOfRiskName | Старна, где акция зарегестрированна  | String     |
| lots              | Лотность акции (1, 10, 10000 и т.п.) | integer    |
| initialOrderPrice       | Начальная цена заявки                | BigDecimal |
| executedOrderPrice      | Выполненная цена заявки              | BigDecimal |
| totalOrderPrice       | Общая стоимость заявки               | BigDecimal |

| Код  | Описание              |
|------|-----------------------|
| 200  | ОК                    |
| 400  | Элемент не существует |
| 404  | Не найдено            |

---
