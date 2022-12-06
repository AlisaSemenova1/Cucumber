# language: ru
# encoding: UTF-8
@withdrawal
Функция: Нажатие чекбокса, проверка

  @success
  Сценарий: Нажать чекбокс, проверить что он нажат
    Дано захожу на страницу "https://the-internet.herokuapp.com/checkboxes"
    Когда нажимаю чекбокс
    Тогда проверяю, что чекбокс нажат



