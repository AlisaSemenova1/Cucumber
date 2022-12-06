# language: ru
# encoding: UTF-8
@withdrawal
Функция: Верная и неверная логинация

@success
Структура сценария: Ввести правильный логин/пароль и неправильный, проверить результат
Дано захожу на "https://the-internet.herokuapp.com/login"
Когда ввожу в соответствующие поля "<username>" и "<password>"
Тогда нажимаю на кнопку логин
Тогда если пароль верен вижу сообщение "You logged into a secure area!"
Тогда если пароль не вререн вижу сообщение "Your username is invalid!"
Примеры:
| username  | password             |
| tomsmith  | SuperSecretPassword! |
| marysmith | NotSecretPassword!   |

