#  **Морской бой**
  
## Добро пожаловать в игру "Морской бой"! Это классическое морское сражение, где ваша цель - уничтожить все корабли противника, размещенные на игровом поле.

### Основные правила игры

    Игровое поле представляет собой матрицу 16x16, где колонки подписаны буквами A-P, а строки цифрами 1-16.
    В игре участвуют 6 типов кораблей: 1 корабль на 6 клеток, 2 корабля на 5 клеток, 3 корабля на 4 клетки, 4 корабля на 3 клетки, 5 кораблей на 2 клетки и 6 кораблей на 1 клетку.
    Игроки могут играть как против бота, так и друг с другом. Каждый игрок расставляет свои корабли на поле в соответствии с правилами.

### Инструкции по запуску

    При запуске игры введите свое имя.
    После имени, выберите, хотите ли пользоваться подсказками, при положительном ответе, вы сможете выбрать насколько полезными будут подсказки.(в данной программе подсказки означает, что ИИ совершать ходы за вас, вам останется только наблюдать)
    Выберите режим игры: "Одиночная игра" или "С напарником".
    Если выбран режим "Одиночная игра", бот автоматически управляет соперником. 
    В режиме игры "С напарником", вы можете выбрать "За одним пк" или "По локальной сети(Websocket)" второй игрок может подключиться через файловую систему или WebSocket.
    Расставьте свои корабли на поле, следуя инструкциям, либо предоставьте игре выставить их рандомно.
    Начинайте игру и делайте ходы, стреляя по координатам противника.
    Игра продолжается до уничтожения всех кораблей противника.

### Особенности режима "С напарником"

    В этом режиме игроки могут играть друг против друга, подключаясь через файловую систему или WebSocket.
    Первый игрок создает серверное подключение с указанным портом.
    Второй игрок подключается к серверу, если игра еще не начата, или наблюдает за текущей партией.

### Требования к игрокам

    В начале каждой партии игроки должны расставить свои корабли на поле.
    Команды для управления кораблями вводятся с консоли, включая координаты, направление и размер корабля.
    Действия игрока валидируются, и в случае ошибки игрок уведомляется о неверных действиях.

### Режим игрового бота

    Игровой бот предоставляет реалистичные ходы, сосредотачиваясь на стратегиях завершения атак и поиске кораблей.
    Есть два уровня сложности игрового бота:
    1. Лёгкий бот стреляет всё время рандомно, и не имеет возможности добивать раненые корабли.
    2. Сложный бот имеет фунцию добивания раненых кораблей.

### Важно

    Следуйте инструкциям на экране для правильного запуска и управления игрой.
    Всегда придерживайтесь правил игры, чтобы обеспечить честное и увлекательное сражение.

### Приятной игры в "Морской бой"!