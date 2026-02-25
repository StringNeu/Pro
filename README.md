
readme

## 自动获取天气接口

本仓库提供了一个基于 [wttr.in](https://wttr.in) 的免费天气查询 Python 接口，无需 API Key。

详细使用说明请查看 [weather_api/README.md](weather_api/README.md)

### 快速使用

```bash
# 命令行查询
python -m weather_api.weather Beijing
```

```python
from weather_api import get_weather_brief
print(get_weather_brief("Beijing"))
```
