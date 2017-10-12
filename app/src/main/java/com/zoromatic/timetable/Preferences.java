package com.zoromatic.timetable;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

public class Preferences {
	public static final String PREF_NAME = "com.zoromatic.timetable.Preferences";
	public static final String PREF_MAIN_SETTINGS = "mainsettings_";
	public static final String PREF_MAIN_THEME = "maintheme_";
	public static final String PREF_MAIN_COLOR_SCHEME = "maincolorscheme_";
	public static final String PREFS_NAME_SEC = "com.zoromatic.timetable.Preferences.Second";
	public static final String PREF_DATE_KEY = "showdate_";
	public static final String PREF_24HRS_KEY = "show24hrs_";
	public static final String PREF_COLOR_KEY = "color_";
	public static final String PREF_DATEFORMAT_KEY = "dateformat_";
	public static final String PREF_BATTERY_KEY = "showbattery_";
	public static final String PREF_TRANSPARENCY_KEY = "transparency_";
	public static final String PREF_BATTERY_NOTIF = "showbatterynotif_";
	public static final String PREF_BATTERY_NOTIF_KEY = "showbatterynotifkey_";
	public static final String PREF_TEMP_NOTIF = "showtempnotif_";
	public static final String PREF_TEMP_NOTIF_KEY = "showtempnotifkey_";
	public static final String PREF_REFRESH_INTERVAL_KEY = "refreshinterval_";
	public static final String PREF_REFRESH_NOW_KEY = "refreshnow_";
	public static final String PREF_LOCATION_SETTINGS_KEY = "locationsettings_";
	public static final String PREF_TEMP_SCALE_KEY = "tempscale_";
	public static final String PREF_LOCATION_LAT_KEY = "locationlat_";
	public static final String PREF_LOCATION_LON_KEY = "locationlon_";
	public static final String PREF_LOCATION_KEY = "location_";
	public static final String PREF_LOCATION_ID_KEY = "locationid_";
	public static final String PREF_LAST_ALARM_KEY = "lastalarm_";
	public static final String PREF_LAST_REFRESH_KEY = "lastrefresh_";
	public static final String PREF_ABOUT_KEY = "zoromaticabout_";
	public static final String PREF_OPENWEATHER_KEY = "openweatherlink_";
	public static final String PREF_WEATHER_SUCCESS = "weathersuccess_";
	public static final String PREF_FORECAST_SUCCESS = "forecastsuccess_";
	public static final String PREF_REFRESH_WIFI_ONLY = "refreshwifionly_";
	public static final String PREF_LOCK_SCREEN_ADMIN = "lockscreenadmin_";
	public static final String PREF_CLOCK_SKIN = "clockskin_";
	public static final String PREF_SOUND_OPTIONS = "soundoptions_";
	public static final String PREF_SOUND_OPTIONS_KEY = "soundoptionskey_";
	public static final String PREF_LANGUAGE_OPTIONS = "languageoptions_";
	public static final String PREF_LANGUAGE_OPTIONS_KEY = "languageoptionskey_";
	public static final String PREF_RESTART_SERVICE = "restartservice_";
	public static final String PREF_WEATHER_ICONS_KEY = "weathericons_";
	public static final String PREF_FONT_KEY = "font_";
	public static final String PREF_BOLD_TEXT_KEY = "boldtext_";
	public static final String PREF_SHOW_WEATHER_KEY = "showweather_";
	public static final String PREF_WEATHER_SETTINGS_KEY = "weathersettings_";
	public static final String PREF_DATE_SETTINGS_KEY = "datesettings_";
	public static final String PREF_LOCATION_TYPE_KEY = "locationtype_";
	public static final String PREF_BATTERY_ICONS = "batteryicons_";
	public static final String PREF_BATTERY_ICONS_KEY = "batteryiconskey_";
	public static final String PREF_WEATHER_COLOR_KEY = "weathercolor_";
	public static final String PREF_WEATHER_FONT_KEY = "weatherfont_";
	public static final String PREF_WEATHER_BOLD_TEXT_KEY = "weatherboldtext_";
	public static final String PREF_DATE_COLOR_KEY = "datecolor_";
	public static final String PREF_DATE_FONT_KEY = "datefont_";
	public static final String PREF_DATE_BOLD_TEXT_KEY = "dateboldtext_";
	public static final String PREF_WEATHER_LAYOUT_KEY = "weatherlayout_";
	public static final String PREF_FORECAST_THEME = "forecasttheme_";
	public static final String PREF_WIDGET_COLOR_KEY = "widgetcolor_";
	public static final String PREF_CLOCK_COLOR_PICKER_KEY = "clockcolorpicker_";
	public static final String PREF_DATE_COLOR_PICKER_KEY = "datecolorpicker_";
	public static final String PREF_WEATHER_COLOR_PICKER_KEY = "weathercolorpicker_";
	public static final String PREF_WIDGET_COLOR_PICKER_KEY = "widgetcolorpicker_";
	public static final String PREF_SHOW_BLUETOOTH_KEY = "showbluetooth_";
	public static final String PREF_SHOW_GPS_KEY = "showgps_";
	public static final String PREF_SHOW_MOBILE_KEY = "showmobile_";
	public static final String PREF_SHOW_RINGER_KEY = "showringer_";
	public static final String PREF_SHOW_WIFI_KEY = "showwifi_";
	public static final String PREF_SHOW_AIRPLANE_KEY = "showairplane_";
	public static final String PREF_SHOW_BRIGHTNESS_KEY = "showbrightness_";
	public static final String PREF_SHOW_NFC_KEY = "shownfc_";
	public static final String PREF_SHOW_SYNC_KEY = "showsync_";
	public static final String PREF_SHOW_ORIENTATION_KEY = "showorientation_";
	public static final String PREF_SHOW_TORCH_KEY = "showtorch_";
	public static final String PREF_SHOW_BATTERY_STATUS_KEY = "showbatterystatus_";
	public static final String PREF_SHOW_SETTINGS_KEY = "showsettings_";
	public static final String PREF_COLOR_ON_KEY = "coloron_";
	public static final String PREF_COLOR_OFF_KEY = "coloroff_";
	public static final String PREF_COLOR_TRANSITION_KEY = "colortransition_";
	public static final String PREF_COLOR_BACKGROUND_KEY = "colorbackground_";
	public static final String PREF_POWER_TRANSPARENCY_KEY = "powertransparency_";
	public static final String PREF_COLOR_TEXT_ON_KEY = "colortexton_";
	public static final String PREF_COLOR_TEXT_OFF_KEY = "colortextoff_";
	public static final String PREF_COLOR_BATTERY1_KEY = "colorbattery1_";
	public static final String PREF_COLOR_BATTERY2_KEY = "colorbattery2_";
	public static final String PREF_COLOR_BATTERY3_KEY = "colorbattery3_";
	public static final String PREF_COLOR_BATTERY4_KEY = "colorbattery4_";
	public static final String PREF_THRESHOLD_BATTERY2_KEY = "thresholdbattery2_";
	public static final String PREF_THRESHOLD_BATTERY3_KEY = "thresholdbattery3_";
	public static final String PREF_THRESHOLD_BATTERY4_KEY = "thresholdbattery4_";
	public static final String PREF_BRIGHTNESS_OPTIONS_KEY = "brightnessoptions_";
	
	private static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(PREF_NAME, 0);
	}

	public static boolean getShowDate(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(PREF_DATE_KEY + appWidgetId,
				false);
	}

	public static boolean getShow24Hrs(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(PREF_24HRS_KEY + appWidgetId,
				true);
	}

	public static int getClockColorItem(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_COLOR_KEY + appWidgetId, -1); // default: -1 for transition to new Color Picker
	}

	public static int getDateFormatItem(Context context, int appWidgetId) {
		return getPreferences(context).getInt(
				PREF_DATEFORMAT_KEY + appWidgetId, 0);
	}

	public static boolean getShowBattery(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(
				PREF_BATTERY_KEY + appWidgetId, false);
	}
	
	public static int getOpacity(Context context, int appWidgetId) {
		return getPreferences(context).getInt(
				PREF_TRANSPARENCY_KEY + appWidgetId, 100);
	}

	public static boolean getShowBatteryNotif(Context context) {
		return getPreferences(context)
				.getBoolean(PREF_BATTERY_NOTIF_KEY, false);
	}
	
	public static boolean getShowTempNotif(Context context) {
		return getPreferences(context)
				.getBoolean(PREF_TEMP_NOTIF_KEY, false);
	}
	
	public static int getRefreshInterval(Context context, int appWidgetId) {
		return getPreferences(context).getInt(
				PREF_REFRESH_INTERVAL_KEY + appWidgetId, 3);
	}
	
	public static int getTempScale(Context context, int appWidgetId) {
		return getPreferences(context).getInt(
				PREF_TEMP_SCALE_KEY + appWidgetId, 0);
	}
	
	public static float getLocationLat(Context context, int appWidgetId) {
		return getPreferences(context).getFloat(
				PREF_LOCATION_LAT_KEY + appWidgetId, -222);
	}
	
	public static float getLocationLon(Context context, int appWidgetId) {
		return getPreferences(context).getFloat(
				PREF_LOCATION_LON_KEY + appWidgetId, -222);
	}
	
	public static String getLocation(Context context, int appWidgetId) {
		return getPreferences(context).getString(
				PREF_LOCATION_KEY + appWidgetId, "");
	}
	
	public static long getLocationId(Context context, int appWidgetId) {
		return getPreferences(context).getLong(
				PREF_LOCATION_ID_KEY + appWidgetId, -1);
	}
	
	public static long getLastAlarm(Context context, int appWidgetId) {
		return getPreferences(context).getLong(
				PREF_LAST_ALARM_KEY + appWidgetId, 0);
	}
	
	public static long getLastRefresh(Context context, int appWidgetId) {
		return getPreferences(context).getLong(
				PREF_LAST_REFRESH_KEY + appWidgetId, 0);
	}
	
	public static boolean getWeatherSuccess(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(
				PREF_WEATHER_SUCCESS + appWidgetId, true);
	}
	
	public static boolean getForecastSuccess(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(
				PREF_FORECAST_SUCCESS + appWidgetId, true);
	}
	
	public static boolean getRefreshWiFiOnly(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(
				PREF_REFRESH_WIFI_ONLY + appWidgetId, true);
	}
	
	public static int getClockSkin(Context context, int appWidgetId) {
		return getPreferences(context).getInt(
				PREF_CLOCK_SKIN + appWidgetId, 2); // default: none
	}
	
	public static String getMainTheme(Context context) {
		return getPreferences(context).getString(
				PREF_MAIN_THEME+0, "light"); // default: light
	}
	
	public static int getMainColorScheme(Context context) {
		return getPreferences(context).getInt(
				PREF_MAIN_COLOR_SCHEME+0, 9); // default: cyan
	}
	
	public static String getForecastTheme(Context context, int appWidgetId) {
		return getPreferences(context).getString(
				PREF_FORECAST_THEME + appWidgetId, "light"); // default: dark
	}
	
	public static int getSoundOptions(Context context) {
		return getPreferences(context)
				.getInt(PREF_SOUND_OPTIONS_KEY, 0);
	}
	
	public static String getLanguageOptions(Context context) {
		return getPreferences(context)
				.getString(PREF_LANGUAGE_OPTIONS_KEY, "");
	}
	
	public static int getWeatherIcons(Context context, int appWidgetId) {
		return getPreferences(context).getInt(
				PREF_WEATHER_ICONS_KEY + appWidgetId, 0);
	}
	
	public static int getFontItem(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_FONT_KEY + appWidgetId, 0);
	}
	
	public static boolean getBoldText(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(
				PREF_BOLD_TEXT_KEY + appWidgetId, true);
	}
	
	public static boolean getShowWeather(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(
				PREF_SHOW_WEATHER_KEY + appWidgetId, true);
	}
	
	public static int getLocationType(Context context, int appWidgetId) {
		return getPreferences(context).getInt(
				PREF_LOCATION_TYPE_KEY + appWidgetId, 0);
	}
	
	public static int getBatteryIcons(Context context) {
		return getPreferences(context).getInt(
				PREF_BATTERY_ICONS_KEY, 0);
	}
	
	public static int getWeatherColorItem(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_WEATHER_COLOR_KEY + appWidgetId, -1); // default: -1 for transition to new Color Picker
	}
	
	public static int getWeatherFontItem(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_WEATHER_FONT_KEY + appWidgetId, 0);
	}
	
	public static boolean getWeatherBoldText(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(
				PREF_WEATHER_BOLD_TEXT_KEY + appWidgetId, true);
	}
	
	public static int getDateColorItem(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_DATE_COLOR_KEY + appWidgetId, -1); // default: -1 for transition to new Color Picker
	}
	
	public static int getDateFontItem(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_DATE_FONT_KEY + appWidgetId, 0);
	}
	
	public static boolean getDateBoldText(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(
				PREF_DATE_BOLD_TEXT_KEY + appWidgetId, true);
	}
	
	public static int getWidgetColorItem(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_WIDGET_COLOR_KEY + appWidgetId, -1); // default: -1 for transition to new Color Picker
	}
	
	public static int getClockColor(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_CLOCK_COLOR_PICKER_KEY + appWidgetId, Color.WHITE); // white
	}
	
	public static int getDateColor(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_DATE_COLOR_PICKER_KEY + appWidgetId, Color.WHITE); // white
	}
	
	public static int getWeatherColor(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_WEATHER_COLOR_PICKER_KEY + appWidgetId, Color.WHITE); // white
	}
	
	public static int getWidgetColor(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_WIDGET_COLOR_PICKER_KEY + appWidgetId, Color.BLACK); // black
	}
	
	public static boolean getShowBluetooth(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(PREF_SHOW_BLUETOOTH_KEY + appWidgetId,
				true);
	}
	
	public static boolean getShowGps(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(PREF_SHOW_GPS_KEY + appWidgetId,
				false);
	}
	
	public static boolean getShowMobile(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(PREF_SHOW_MOBILE_KEY + appWidgetId,
				false);
	}
	
	public static boolean getShowRinger(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(PREF_SHOW_RINGER_KEY + appWidgetId,
				true);
	}
	
	public static boolean getShowWiFi(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(PREF_SHOW_WIFI_KEY + appWidgetId,
				true);
	}
	
	public static boolean getShowAirplane(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(PREF_SHOW_AIRPLANE_KEY + appWidgetId,
				false);
	}
	
	public static boolean getShowBrightness(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(PREF_SHOW_BRIGHTNESS_KEY + appWidgetId,
				true);
	}
	
	public static boolean getShowNfc(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(PREF_SHOW_NFC_KEY + appWidgetId,
				false);
	}
	
	public static boolean getShowSync(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(PREF_SHOW_SYNC_KEY + appWidgetId,
				false);
	}
	
	public static boolean getShowOrientation(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(PREF_SHOW_ORIENTATION_KEY + appWidgetId,
				false);
	}
	
	public static boolean getShowTorch(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(PREF_SHOW_TORCH_KEY + appWidgetId,
				false);
	}
	
	public static boolean getShowBatteryStatus(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(PREF_SHOW_BATTERY_STATUS_KEY + appWidgetId,
				true);
	}
	
	public static boolean getShowSettings(Context context, int appWidgetId) {
		return getPreferences(context).getBoolean(PREF_SHOW_SETTINGS_KEY + appWidgetId,
				false);
	}
	
	public static int getColorOn(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_COLOR_ON_KEY + appWidgetId,
				0xFF35B6E5);
	}
	
	public static int getColorOff(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_COLOR_OFF_KEY + appWidgetId,
				0xFFC0C0C0);
	}
	
	public static int getColorTransition(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_COLOR_TRANSITION_KEY + appWidgetId,
				0xFFFF8C00);
	}
	
	public static int getColorBackground(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_COLOR_BACKGROUND_KEY + appWidgetId,
				0xFF000000);
	}
	
	public static int getPowerOpacity(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_POWER_TRANSPARENCY_KEY + appWidgetId,
				100);
	}
	
	public static int getColorTextOn(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_COLOR_TEXT_ON_KEY + appWidgetId,
				0xFFFFFFFF);
	}
	
	public static int getColorTextOff(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_COLOR_TEXT_OFF_KEY + appWidgetId,
				0xFFFFFFFF);
	}
	
	public static int getColorBattery1(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_COLOR_BATTERY1_KEY + appWidgetId,
				0xFFFF0000);
	}
	
	public static int getColorBattery2(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_COLOR_BATTERY2_KEY + appWidgetId,
				0xFFFF6A00);
	}
	
	public static int getColorBattery3(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_COLOR_BATTERY3_KEY + appWidgetId,
				0xFFB6FF00);
	}
	
	public static int getColorBattery4(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_COLOR_BATTERY4_KEY + appWidgetId,
				0xFF00FF21);
	}
	
	public static int getThresholdBattery2(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_THRESHOLD_BATTERY2_KEY + appWidgetId,
				25);
	}
	
	public static int getThresholdBattery3(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_THRESHOLD_BATTERY2_KEY + appWidgetId,
				50);
	}
	
	public static int getThresholdBattery4(Context context, int appWidgetId) {
		return getPreferences(context).getInt(PREF_THRESHOLD_BATTERY2_KEY + appWidgetId,
				75);
	}
	
	public static int getBrightnessOptions(Context context) {
		return getPreferences(context)
				.getInt(PREF_BRIGHTNESS_OPTIONS_KEY+0, 0);
	}

	public static void setShowDate(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
				.putBoolean(PREF_DATE_KEY + appWidgetId, value).commit();
	}

	public static void setShow24Hrs(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
				.putBoolean(PREF_24HRS_KEY + appWidgetId, value).commit();
	}

	public static void setClockColorItem(Context context, int appWidgetId, int value) {
		getPreferences(context).edit()
				.putInt(PREF_COLOR_KEY + appWidgetId, value).commit();
	}

	public static void setDateFormatItem(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
				.putInt(PREF_DATEFORMAT_KEY + appWidgetId, value).commit();
	}

	public static void setShowBattery(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
				.putBoolean(PREF_BATTERY_KEY + appWidgetId, value).commit();
	}
	
	public static void setOpacity(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
				.putInt(PREF_TRANSPARENCY_KEY + appWidgetId, value).commit();
	}

	public static void setShowBatteryNotif(Context context, boolean value) {
		getPreferences(context).edit()
				.putBoolean(PREF_BATTERY_NOTIF_KEY, value).commit();
	}
	
	public static void setShowTempNotif(Context context, boolean value) {
		getPreferences(context).edit()
				.putBoolean(PREF_TEMP_NOTIF_KEY, value).commit();
	}
	
	public static void setRefreshInterval(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
				.putInt(PREF_REFRESH_INTERVAL_KEY + appWidgetId, value).commit();
	}
	
	public static void setTempScale(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
				.putInt(PREF_TEMP_SCALE_KEY + appWidgetId, value).commit();
	}
	
	public static void setLocationLat(Context context, int appWidgetId,
			float value) {
		getPreferences(context).edit()
				.putFloat(PREF_LOCATION_LAT_KEY + appWidgetId, value).commit();
	}
	
	public static void setLocationLon(Context context, int appWidgetId,
			float value) {
		getPreferences(context).edit()
				.putFloat(PREF_LOCATION_LON_KEY + appWidgetId, value).commit();
	}
	
	public static void setLocation(Context context, int appWidgetId,
			String value) {
		getPreferences(context).edit()
				.putString(PREF_LOCATION_KEY + appWidgetId, value).commit();
	}
	
	public static void setLocationId(Context context, int appWidgetId,
			long value) {
		getPreferences(context).edit()
				.putLong(PREF_LOCATION_ID_KEY + appWidgetId, value).commit();
	}
	
	public static void setLastAlarm(Context context, int appWidgetId,
			long value) {
		getPreferences(context).edit()
				.putLong(PREF_LAST_ALARM_KEY + appWidgetId, value).commit();
	}
	
	public static void setLastRefresh(Context context, int appWidgetId,
			long value) {
		getPreferences(context).edit()
				.putLong(PREF_LAST_REFRESH_KEY + appWidgetId, value).commit();
	}
	
	public static void setWeatherSuccess(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
				.putBoolean(PREF_WEATHER_SUCCESS + appWidgetId, value).commit();
	}
	
	public static void setForecastSuccess(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
				.putBoolean(PREF_FORECAST_SUCCESS + appWidgetId, value).commit();
	}
	
	public static void setRefreshWiFiOnly(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
				.putBoolean(PREF_REFRESH_WIFI_ONLY + appWidgetId, value).commit();
	}
	
	public static void setClockSkin(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
				.putInt(PREF_CLOCK_SKIN + appWidgetId, value).commit();
	}
	
	public static void setMainTheme(Context context, String value) {
		getPreferences(context).edit()
				.putString(PREF_MAIN_THEME+0, value).commit();
	}
	
	public static void setMainColorScheme(Context context, int value) {
		getPreferences(context).edit()
				.putInt(PREF_MAIN_COLOR_SCHEME+0, value).commit();
	}
	
	public static void setForecastTheme(Context context, int appWidgetId, String value) {
		getPreferences(context).edit()
				.putString(PREF_FORECAST_THEME + appWidgetId, value).commit();
	}
	
	public static void setSoundOptions(Context context, int value) {
		getPreferences(context).edit()
				.putInt(PREF_SOUND_OPTIONS_KEY, value).commit();
	}
	
	public static void setLanguageOptions(Context context, String value) {
		getPreferences(context).edit()
				.putString(PREF_LANGUAGE_OPTIONS_KEY, value).commit();
	}
	
	public static void setWeatherIcons(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
				.putInt(PREF_WEATHER_ICONS_KEY + appWidgetId, value).commit();
	}
	
	public static void setFontItem(Context context, int appWidgetId, int value) {
		getPreferences(context).edit()
				.putInt(PREF_FONT_KEY + appWidgetId, value).commit();
	}
	
	public static void setBoldText(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
				.putBoolean(PREF_BOLD_TEXT_KEY + appWidgetId, value).commit();
	}
	
	public static void setShowWeather(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
				.putBoolean(PREF_SHOW_WEATHER_KEY + appWidgetId, value).commit();
	}
	
	public static void setLocationType(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
				.putInt(PREF_LOCATION_TYPE_KEY + appWidgetId, value).commit();
	}
	
	public static void setBatteryIcons(Context context, int value) {
		getPreferences(context).edit()
				.putInt(PREF_BATTERY_ICONS_KEY, value).commit();
	}
	
	public static void setWeatherColorItem(Context context, int appWidgetId, int value) {
		getPreferences(context).edit()
				.putInt(PREF_WEATHER_COLOR_KEY + appWidgetId, value).commit();
	}
	
	public static void setWeatherFontItem(Context context, int appWidgetId, int value) {
		getPreferences(context).edit()
				.putInt(PREF_WEATHER_FONT_KEY + appWidgetId, value).commit();
	}
	
	public static void setWeatherBoldText(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
				.putBoolean(PREF_WEATHER_BOLD_TEXT_KEY + appWidgetId, value).commit();
	}
	
	public static void setDateColorItem(Context context, int appWidgetId, int value) {
		getPreferences(context).edit()
				.putInt(PREF_DATE_COLOR_KEY + appWidgetId, value).commit();
	}
	
	public static void setDateFontItem(Context context, int appWidgetId, int value) {
		getPreferences(context).edit()
				.putInt(PREF_DATE_FONT_KEY + appWidgetId, value).commit();
	}
	
	public static void setDateBoldText(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
				.putBoolean(PREF_DATE_BOLD_TEXT_KEY + appWidgetId, value).commit();
	}
	
	public static void setWidgetColorItem(Context context, int appWidgetId, int value) {
		getPreferences(context).edit()
				.putInt(PREF_WIDGET_COLOR_KEY + appWidgetId, value).commit();
	}
	
	public static void setClockColor(Context context, int appWidgetId, int value) {
		getPreferences(context).edit()
				.putInt(PREF_CLOCK_COLOR_PICKER_KEY + appWidgetId, value).commit();
	}
	
	public static void setDateColor(Context context, int appWidgetId, int value) {
		getPreferences(context).edit()
				.putInt(PREF_DATE_COLOR_PICKER_KEY + appWidgetId, value).commit();
	}
	
	public static void setWeatherColor(Context context, int appWidgetId, int value) {
		getPreferences(context).edit()
				.putInt(PREF_WEATHER_COLOR_PICKER_KEY + appWidgetId, value).commit();
	}
	
	public static void setWidgetColor(Context context, int appWidgetId, int value) {
		getPreferences(context).edit()
				.putInt(PREF_WIDGET_COLOR_PICKER_KEY + appWidgetId, value).commit();
	}
	
	public static void setShowBluetooth(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
		.putBoolean(PREF_SHOW_BLUETOOTH_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setShowGps(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
		.putBoolean(PREF_SHOW_GPS_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setShowMobile(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
		.putBoolean(PREF_SHOW_MOBILE_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setShowRinger(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
		.putBoolean(PREF_SHOW_RINGER_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setShowWiFi(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
		.putBoolean(PREF_SHOW_WIFI_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setShowAirplane(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
		.putBoolean(PREF_SHOW_AIRPLANE_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setShowBrightness(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
		.putBoolean(PREF_SHOW_BRIGHTNESS_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setShowNfc(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
		.putBoolean(PREF_SHOW_NFC_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setShowSync(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
		.putBoolean(PREF_SHOW_SYNC_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setShowOrientation(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
		.putBoolean(PREF_SHOW_ORIENTATION_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setShowTorch(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
		.putBoolean(PREF_SHOW_TORCH_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setShowBatteryStatus(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
		.putBoolean(PREF_SHOW_BATTERY_STATUS_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setShowSettings(Context context, int appWidgetId,
			boolean value) {
		getPreferences(context).edit()
		.putBoolean(PREF_SHOW_SETTINGS_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setColorOn(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
		.putInt(PREF_COLOR_ON_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setColorOff(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
		.putInt(PREF_COLOR_OFF_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setColorTransition(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
		.putInt(PREF_COLOR_TRANSITION_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setColorBackground(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
		.putInt(PREF_COLOR_BACKGROUND_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setPowerOpacity(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
		.putInt(PREF_POWER_TRANSPARENCY_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setColorTextOn(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
		.putInt(PREF_COLOR_TEXT_ON_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setColorTextOff(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
		.putInt(PREF_COLOR_TEXT_OFF_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setColorBattery1(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
		.putInt(PREF_COLOR_BATTERY1_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setColorBattery2(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
		.putInt(PREF_COLOR_BATTERY2_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setColorBattery3(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
		.putInt(PREF_COLOR_BATTERY3_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setColorBattery4(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
		.putInt(PREF_COLOR_BATTERY4_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setThresholdBattery2(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
		.putInt(PREF_THRESHOLD_BATTERY2_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setThresholdBattery3(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
		.putInt(PREF_THRESHOLD_BATTERY3_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setThresholdBattery4(Context context, int appWidgetId,
			int value) {
		getPreferences(context).edit()
		.putInt(PREF_THRESHOLD_BATTERY4_KEY + appWidgetId,
				value).commit();
	}
	
	public static void setBrightnessOptions(Context context, int value) {
		getPreferences(context).edit()
				.putInt(PREF_BRIGHTNESS_OPTIONS_KEY+0, value).commit();
	}
}
