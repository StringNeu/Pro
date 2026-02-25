"""weather_api 单元测试"""

import json
import unittest
from unittest.mock import patch, MagicMock
from io import BytesIO

from weather_api.weather import WeatherAPI, WeatherAPIError, get_weather, get_weather_brief


# 模拟 wttr.in 返回的 JSON 数据
MOCK_RESPONSE = {
    "current_condition": [
        {
            "temp_C": "25",
            "FeelsLikeC": "27",
            "humidity": "40",
            "weatherDesc": [{"value": "Sunny"}],
            "lang_zh": [{"value": "晴"}],
            "windspeedKmph": "15",
            "winddir16Point": "NE",
            "visibility": "10",
            "pressure": "1015",
            "uvIndex": "5",
            "observation_time": "12:00 PM",
        }
    ],
    "nearest_area": [
        {
            "areaName": [{"value": "Beijing"}],
            "country": [{"value": "China"}],
        }
    ],
    "weather": [
        {
            "date": "2026-02-25",
            "maxtempC": "28",
            "mintempC": "15",
            "avgtempC": "22",
            "totalSnow_cm": "0.0",
            "sunHour": "8.0",
            "uvIndex": "5",
            "hourly": [
                {
                    "weatherDesc": [{"value": "Sunny"}],
                    "lang_zh": [{"value": "晴"}],
                }
            ],
        },
        {
            "date": "2026-02-26",
            "maxtempC": "26",
            "mintempC": "14",
            "avgtempC": "20",
            "totalSnow_cm": "0.0",
            "sunHour": "7.0",
            "uvIndex": "4",
            "hourly": [
                {
                    "weatherDesc": [{"value": "Cloudy"}],
                    "lang_zh": [{"value": "多云"}],
                }
            ],
        },
        {
            "date": "2026-02-27",
            "maxtempC": "24",
            "mintempC": "12",
            "avgtempC": "18",
            "totalSnow_cm": "0.0",
            "sunHour": "6.0",
            "uvIndex": "3",
            "hourly": [
                {
                    "weatherDesc": [{"value": "Rain"}],
                    "lang_zh": [{"value": "小雨"}],
                }
            ],
        },
    ],
}


def _mock_urlopen(req, timeout=10):
    """模拟 urllib.request.urlopen 返回"""
    resp = MagicMock()
    resp.read.return_value = json.dumps(MOCK_RESPONSE).encode("utf-8")
    resp.__enter__ = lambda s: s
    resp.__exit__ = MagicMock(return_value=False)
    return resp


class TestWeatherAPI(unittest.TestCase):
    """测试 WeatherAPI 类"""

    @patch("weather_api.weather.urllib.request.urlopen", side_effect=_mock_urlopen)
    def test_get_current_weather(self, mock_open):
        api = WeatherAPI()
        result = api.get_current_weather("Beijing")

        self.assertEqual(result["temp_C"], "25")
        self.assertEqual(result["feels_like_C"], "27")
        self.assertEqual(result["humidity"], "40")
        self.assertEqual(result["weather_desc"], "晴")
        self.assertEqual(result["location"], "Beijing, China")
        self.assertEqual(result["wind_speed_kmph"], "15")
        self.assertEqual(result["uv_index"], "5")

    @patch("weather_api.weather.urllib.request.urlopen", side_effect=_mock_urlopen)
    def test_get_forecast(self, mock_open):
        api = WeatherAPI()
        result = api.get_forecast("Beijing", days=3)

        self.assertEqual(len(result), 3)
        self.assertEqual(result[0]["date"], "2026-02-25")
        self.assertEqual(result[0]["max_temp_C"], "28")
        self.assertEqual(result[0]["min_temp_C"], "15")
        self.assertEqual(result[0]["weather_desc"], "晴")
        self.assertEqual(result[1]["weather_desc"], "多云")
        self.assertEqual(result[2]["weather_desc"], "小雨")

    @patch("weather_api.weather.urllib.request.urlopen", side_effect=_mock_urlopen)
    def test_get_forecast_limited_days(self, mock_open):
        api = WeatherAPI()
        result = api.get_forecast("Beijing", days=1)
        self.assertEqual(len(result), 1)

    @patch("weather_api.weather.urllib.request.urlopen", side_effect=_mock_urlopen)
    def test_get_raw(self, mock_open):
        api = WeatherAPI()
        result = api.get_raw("Beijing")
        self.assertIn("current_condition", result)
        self.assertIn("weather", result)

    @patch("weather_api.weather.urllib.request.urlopen", side_effect=_mock_urlopen)
    def test_get_weather_function(self, mock_open):
        result = get_weather("Beijing")
        self.assertEqual(result["temp_C"], "25")
        self.assertEqual(result["weather_desc"], "晴")

    @patch("weather_api.weather.urllib.request.urlopen", side_effect=_mock_urlopen)
    def test_get_weather_brief_function(self, mock_open):
        result = get_weather_brief("Beijing")
        self.assertIn("Beijing", result)
        self.assertIn("25°C", result)
        self.assertIn("晴", result)

    def test_network_error_raises_weather_api_error(self):
        import urllib.error
        with patch(
            "weather_api.weather.urllib.request.urlopen",
            side_effect=urllib.error.URLError("Connection refused"),
        ):
            api = WeatherAPI()
            with self.assertRaises(WeatherAPIError):
                api.get_current_weather("Beijing")

    def test_http_error_raises_weather_api_error(self):
        import urllib.error
        with patch(
            "weather_api.weather.urllib.request.urlopen",
            side_effect=urllib.error.HTTPError(
                url="http://test", code=404, msg="Not Found", hdrs={}, fp=None
            ),
        ):
            api = WeatherAPI()
            with self.assertRaises(WeatherAPIError):
                api.get_current_weather("InvalidCity")

    @patch("weather_api.weather.urllib.request.urlopen", side_effect=_mock_urlopen)
    def test_custom_lang(self, mock_open):
        api = WeatherAPI(lang="en")
        self.assertEqual(api.lang, "en")
        api.get_current_weather("Beijing")
        mock_open.assert_called_once()

    def test_days_clamped(self):
        """days 参数应被限制在 1~3"""
        api = WeatherAPI()
        with patch("weather_api.weather.urllib.request.urlopen", side_effect=_mock_urlopen):
            result = api.get_forecast("Beijing", days=10)
            self.assertLessEqual(len(result), 3)

            result = api.get_forecast("Beijing", days=0)
            self.assertLessEqual(len(result), 1)


if __name__ == "__main__":
    unittest.main()
