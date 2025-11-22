# Observations Statistics  
_Проєкт для багатопоточної обробки астрономічних спостережень_

## Опис проєкту
Програма аналізує набір JSON-файлів з астрономічними спостереженнями та обчислює статистику за одним із полів:
- `tags`
- `author`
- `location`
- `instrument`

Користувач вказує:
1) Шлях до директорії з даними  
2) Назву поля  
3) Кількість потоків  

Після обробки програма створює відповідний XML-файл зі статистикою.

---

# Основні сутності

### **Observation**
Опис одного спостереження, отриманого з JSON-файлу. Містить поля:
- `id` — ідентифікатор спостереження  
- `author` — автор спостереження  
- `location` — місце здійснення  
- `timestamp` — дата та час  
- `instrument` — інструмент  
- `tags` — список тегів  

### **StatisticsCalculator**
Клас, який виконує паралельну агрегацію значень:
- читає всі JSON-файли  
- видобуває потрібне поле  
- підраховує кількість повторень  
- зливає результати між потоками  

### **XmlWriter**
Генерує підсумковий XML-файл у форматі:

```xml
<statistics field="tags">
    <value name="galaxy" count="12"/>
    <value name="nebula" count="7"/>
    ...
</statistics>
```
## Формат даних

### Вхідні файли (`data/*.json`)
```json
{
  "id": "obs_0123",
  "author": "John Doe",
  "location": "Chile",
  "timestamp": "2024-05-11T23:14:52",
  "instrument": "Canon R6",
  "tags": ["nebula", "deep_sky", "rgb"]
}
```
### Вихідний XML
```xml
<statistics field="tags">
    <value name="nebula" count="15"/>
    <value name="rgb" count="12"/>
    <value name="deep_sky" count="19"/>
</statistics>
```

**Автоматично створювані файли статистики:**
- `statistics_by_tags.xml`
- `statistics_by_author.xml`
- `statistics_by_location.xml`
- `statistics_by_instrument.xml`

---

## Запуск
### Збірка проєкту (після завантаження даного рипозиторію (https://github.com/OlesiaShevchenko245/Internship_Task1))
```
mvn clean package
```

Результат: `target/observations-statistics-1.0-SNAPSHOT.jar`

### Запуск
```
java -jar observations-statistics.jar <data-folder> <field> <threads>   
```

#### Приклад
```bash
java -jar target/observations-statistics.jar data tags 8
```

**Параметри:**
- `<data-folder>` — шлях до директорії з JSON-файлами
- `<field>` — поле для аналізу (tags, author, location, instrument, timestamp)
- `<threads>` — кількість потоків для обробки

---

## Результати тестування (приклади 4/8 потоків)

> **Тестове середовище:** Mackbook M2 Air  
> **Обсяг даних:** ~100 JSON-файлів

### Поле: `tags` (83 унікальні значення)

| Потоки | Час виконання | 
|--------|---------------|
| 4      | 22–24 ms      | 
| 8      | 22–23 ms      | 

### Поле: `author` (62 унікальні значення)

| Потоки | Час виконання |
|--------|---------------|
| 4      | 22 ms         |
| 8      | 24 ms         |

### Поле: `location` (21 унікальне значення)

| Потоки | Час виконання |
|--------|---------------|
| 4      | 24 ms         |
| 8      | 22 ms         |

### Поле: `instrument` (41 унікальне значення)

| Потоки | Час виконання |
|--------|---------------|
| 4      | 22 ms         |
| 8      | 22 ms         |

---

## Висновки

- На малих обсягах даних (кілька сотень записів) багатопоточність не дає відчутного приросту через накладні витрати на створення потоків
- Стабільний час обробки: 22–24 мс незалежно від кількості потоків
- Незважаючи на це, різниця у часі очікується при:
  - Обсязі 10 000+ спостережень
  - Збільшенні складності обробки (валідація, трансформація даних)
  - Роботі з мережевими ресурсами або БД

---

## Структура проєкту
```
observations-statistics/
│
├── pom.xml                      # Maven конфігурація
├── README.md                    # Документація
│
├── src/
│   ├── main/
│   │   ├── java/               # Вихідний код
│   │   │   ├── model/
│   │   │   │   └── Observation.java
│   │   │   ├── service/
│   │   │   │   ├── StatisticsCalculator.java
│   │   │   │   └── XmlWriter.java
│   │   │   └── Main.java
│   │   └── resources/          # Ресурси
│   └── test/                   # Тести
│
└── data/                       # JSON файли спостережень
    ├── obs1.json
    ├── obs2.json
    └── ...
```
---

## Автор

Проєкт виконала Олеся Шевченко в рамках **Full-Stack Internship** :)
