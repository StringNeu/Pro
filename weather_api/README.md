# 自动获取天气接口 (Weather Auto-Fetch API)

基于 [wttr.in](https://wttr.in) 免费天气服务的 Python 天气查询接口，**无需 API Key**，支持全球城市天气查询。

## 功能特性

- ✅ 获取当前天气（温度、湿度、风速、天气描述等）
- ✅ 获取未来 1\~3 天天气预报
- ✅ 支持中文/英文等多语言
- ✅ 零依赖（仅使用 Python 标准库）
- ✅ 支持命令行直接使用
- ✅ 提供类接口和便捷函数两种调用方式

## 快速开始

### 命令行使用

```bash
# 查询北京天气
python -m weather_api.weather Beijing

# 查询上海天气
python -m weather_api.weather Shanghai
```

### 代码调用

```python
from weather_api import get_weather, get_weather_brief, WeatherAPI

# 方式1: 快速获取天气摘要（一行文本）
print(get_weather_brief("Beijing"))
# 输出: Beijing, China: 晴, 25°C, 体感 27°C, 湿度 40%

# 方式2: 获取当前天气数据（字典）
data = get_weather("Shanghai")
print(data["temp_C"])       # 温度
print(data["humidity"])     # 湿度
print(data["weather_desc"]) # 天气描述

# 方式3: 使用类接口获取完整功能
api = WeatherAPI(lang="zh")

# 当前天气
current = api.get_current_weather("Guangzhou")
print(current)

# 未来3天预报
forecast = api.get_forecast("Shenzhen", days=3)
for day in forecast:
    print(f"{day['date']}: {day['weather_desc']}, {day['min_temp_C']}~{day['max_temp_C']}°C")

# 获取原始 JSON 数据
raw = api.get_raw("Chengdu")
```

## API 文档

### `WeatherAPI` 类

| 方法 | 说明 |
|------|------|
| `get_current_weather(city)` | 获取当前天气，返回结构化字典 |
| `get_forecast(city, days=3)` | 获取多日预报（1\~3天） |
| `get_raw(city)` | 获取 wttr.in 原始 JSON 数据 |

#### 当前天气返回字段

| 字段 | 说明 |
|------|------|
| `location` | 位置 |
| `temp_C` | 温度（°C） |
| `feels_like_C` | 体感温度（°C） |
| `humidity` | 湿度（%） |
| `weather_desc` | 天气描述 |
| `wind_speed_kmph` | 风速（km/h） |
| `wind_dir` | 风向 |
| `visibility_km` | 能见度（km） |
| `pressure_mb` | 气压（mb） |
| `uv_index` | 紫外线指数 |
| `observation_time` | 观测时间 |

### 便捷函数

| 函数 | 说明 |
|------|------|
| `get_weather(city, lang="zh")` | 获取当前天气字典 |
| `get_weather_brief(city, lang="zh")` | 获取一行天气摘要文本 |

## 异常处理

```python
from weather_api.weather import WeatherAPI, WeatherAPIError

api = WeatherAPI()
try:
    weather = api.get_current_weather("Beijing")
except WeatherAPIError as e:
    print(f"获取天气失败: {e}")
```

## 环境要求

- Python 3.6+
- 无第三方依赖
