# language: ru
# encoding: UTF-8
@withdrawal
Функция: Бронирование

  @success
  Структура сценария: Получение токена и вывод токена в консоль
    Дано я ввожу "<username>" и "<password>"
    Когда я отправляю запрос на получение токена
    Тогда я получаю токен
    Примеры:
      | username | password    |
      | admin    | password123 |
      | user     | password123 |


  @success
  Структура сценария: Создание бронирования, проверка по id
    Дано я ввожу "<firstname>"  и "<lastname>"
    Когда я создаю бронирование и получаю id
    Тогда я проверяю бронирование
    И я создаю бронирование с неверным телом
    Тогда проверяю бронирование с неверным телом
    Примеры:
      | firstname | lastname |
      | Иван      | Иванов   |


  @success
  Сценарий: Создать и обновить бронирование, проверить что бронирование обновлено
    Дано авторизуюсь
    Дано я создаю бронирование и получаю id
    Когда обновляю бронирование
    Тогда проверяю, что бронирование обновлено
    И проверяю бронирование с несуществующим id


  @success
  Сценарий: Создать и удалить бронирование, проверить что бронирование удалено, удаление бронирования с несуществующим id
    Дано авторизуюсь
    Дано я создаю бронирование и получаю id
    Когда удаляю бронирование
    Тогда проверяю, что бронирование удалено
    И удаляю бронирование с несуществующим id


