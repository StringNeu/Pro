"""
自动获取天气接口 (Automatic Weather Fetching API)

使用免费的 wttr.in 公共天气服务，无需 API Key。
支持按城市名称查询当前天气及多日预报。

Usage:
    from weather_api import get_weather, get_weather_brief, WeatherAPI

    # 快速获取天气摘要
    print(get_weather_brief("Beijing"))

    # 获取完整天气数据（JSON）
    data = get_weather("Shanghai")

    # 使用类接口
    api = WeatherAPI(lang="zh")
    result = api.get_current_weather("Guangzhou")
    forecast = api.get_forecast("Shenzhen", days=3)
"""

import json
import urllib.request
import urllib.error
import urllib.parse
from typing import Any, Dict, List, Optional


_DEFAULT_BASE_URL = "https://wttr.in"
_DEFAULT_LANG = "zh"
_DEFAULT_TIMEOUT = 10


class WeatherAPIError(Exception):
    """天气接口异常"""
    pass


class WeatherAPI:
    """自动获取天气的接口类

    基于 wttr.in 免费天气服务，支持全球城市天气查询。

    Attributes:
        base_url: 天气服务基础 URL
        lang: 返回结果的语言（默认中文 "zh"）
        timeout: 请求超时时间（秒）
    """

    def __init__(
        self,
        base_url: str = _DEFAULT_BASE_URL,
        lang: str = _DEFAULT_LANG,
        timeout: int = _DEFAULT_TIMEOUT,
    ):
        self.base_url = base_url.rstrip("/")
        self.lang = lang
        self.timeout = timeout

    def _request(self, path: str, params: Optional[Dict[str, str]] = None) -> Any:
        """发送 HTTP 请求并返回 JSON 数据"""
        url = f"{self.base_url}/{urllib.parse.quote(path)}"
        if params:
            url += "?" + urllib.parse.urlencode(params)

        req = urllib.request.Request(
            url,
            headers={
                "Accept": "application/json",
                "Accept-Language": self.lang,
                "User-Agent": "weather-api-python/1.0",
            },
        )

        try:
            with urllib.request.urlopen(req, timeout=self.timeout) as resp:
                data = resp.read().decode("utf-8")
                return json.loads(data)
        except urllib.error.HTTPError as e:
            raise WeatherAPIError(f"HTTP 请求失败: {e.code} {e.reason}") from e
        except urllib.error.URLError as e:
            raise WeatherAPIError(f"网络连接失败: {e.reason}") from e
        except json.JSONDecodeError as e:
            raise WeatherAPIError(f"JSON 解析失败: {e}") from e

    def get_current_weather(self, city: str) -> Dict[str, Any]:
        """获取指定城市的当前天气

        Args:
            city: 城市名称（支持中文或英文，如 "北京" 或 "Beijing"）

        Returns:
            dict: 包含当前天气信息的字典，主要字段:
                - location: 位置信息
                - temp_C: 温度（摄氏度）
                - feels_like_C: 体感温度
                - humidity: 湿度（%）
                - weather_desc: 天气描述
                - wind_speed_kmph: 风速（km/h）
                - wind_dir: 风向
                - visibility_km: 能见度（km）
                - pressure_mb: 气压（mb）
                - uv_index: 紫外线指数
                - observation_time: 观测时间

        Raises:
            WeatherAPIError: 请求失败时抛出
        """
        raw = self._request(city, params={"format": "j1"})

        current = raw.get("current_condition", [{}])[0]
        nearest = raw.get("nearest_area", [{}])[0]

        area_name = ""
        if nearest.get("areaName"):
            area_name = nearest["areaName"][0].get("value", "")
        country = ""
        if nearest.get("country"):
            country = nearest["country"][0].get("value", "")

        weather_desc = ""
        if current.get("lang_zh"):
            weather_desc = current["lang_zh"][0].get("value", "")
        elif current.get("weatherDesc"):
            weather_desc = current["weatherDesc"][0].get("value", "")

        return {
            "location": f"{area_name}, {country}" if country else area_name,
            "temp_C": current.get("temp_C", ""),
            "feels_like_C": current.get("FeelsLikeC", ""),
            "humidity": current.get("humidity", ""),
            "weather_desc": weather_desc,
            "wind_speed_kmph": current.get("windspeedKmph", ""),
            "wind_dir": current.get("winddir16Point", ""),
            "visibility_km": current.get("visibility", ""),
            "pressure_mb": current.get("pressure", ""),
            "uv_index": current.get("uvIndex", ""),
            "observation_time": current.get("observation_time", ""),
        }

    def get_forecast(self, city: str, days: int = 3) -> List[Dict[str, Any]]:
        """获取指定城市的多日天气预报

        Args:
            city: 城市名称
            days: 预报天数（1-3，默认3天）

        Returns:
            list[dict]: 每日预报列表，每项包含:
                - date: 日期
                - max_temp_C: 最高温度
                - min_temp_C: 最低温度
                - avg_temp_C: 平均温度
                - weather_desc: 天气描述
                - total_snow_cm: 降雪量（cm）
                - sun_hour: 日照时长
                - uv_index: 紫外线指数

        Raises:
            WeatherAPIError: 请求失败时抛出
        """
        days = max(1, min(days, 3))
        raw = self._request(city, params={"format": "j1"})

        forecasts = []
        for day in raw.get("weather", [])[:days]:
            weather_desc = ""
            hourly = day.get("hourly", [{}])
            if hourly:
                mid = hourly[len(hourly) // 2]
                if mid.get("lang_zh"):
                    weather_desc = mid["lang_zh"][0].get("value", "")
                elif mid.get("weatherDesc"):
                    weather_desc = mid["weatherDesc"][0].get("value", "")

            forecasts.append({
                "date": day.get("date", ""),
                "max_temp_C": day.get("maxtempC", ""),
                "min_temp_C": day.get("mintempC", ""),
                "avg_temp_C": day.get("avgtempC", ""),
                "weather_desc": weather_desc,
                "total_snow_cm": day.get("totalSnow_cm", ""),
                "sun_hour": day.get("sunHour", ""),
                "uv_index": day.get("uvIndex", ""),
            })

        return forecasts

    def get_raw(self, city: str) -> Dict[str, Any]:
        """获取原始天气数据（完整 JSON）

        Args:
            city: 城市名称

        Returns:
            dict: wttr.in 返回的完整 JSON 数据
        """
        return self._request(city, params={"format": "j1"})


# ── 便捷函数 ──────────────────────────────────────────────


def get_weather(city: str, lang: str = "zh") -> Dict[str, Any]:
    """快速获取指定城市的当前天气（字典格式）

    Args:
        city: 城市名称（如 "Beijing"、"上海"）
        lang: 语言，默认 "zh"（中文）

    Returns:
        dict: 当前天气数据
    """
    api = WeatherAPI(lang=lang)
    return api.get_current_weather(city)


def get_weather_brief(city: str, lang: str = "zh") -> str:
    """快速获取天气摘要（一行文本）

    Args:
        city: 城市名称
        lang: 语言，默认 "zh"

    Returns:
        str: 天气摘要文本，例如 "Beijing: 晴, 25°C, 体感 27°C, 湿度 40%"
    """
    w = get_weather(city, lang=lang)
    return (
        f"{w['location']}: {w['weather_desc']}, "
        f"{w['temp_C']}°C, 体感 {w['feels_like_C']}°C, "
        f"湿度 {w['humidity']}%"
    )


# ── 命令行入口 ────────────────────────────────────────────

if __name__ == "__main__":
    import sys

    city = sys.argv[1] if len(sys.argv) > 1 else "Beijing"
    print(f"正在查询 {city} 的天气...\n")

    try:
        api = WeatherAPI()

        print("【当前天气】")
        current = api.get_current_weather(city)
        for key, val in current.items():
            print(f"  {key}: {val}")

        print("\n【未来3天预报】")
        forecast = api.get_forecast(city, days=3)
        for day in forecast:
            print(f"  {day['date']}: {day['weather_desc']}, "
                  f"{day['min_temp_C']}~{day['max_temp_C']}°C")

    except WeatherAPIError as e:
        print(f"获取天气失败: {e}", file=sys.stderr)
        sys.exit(1)
