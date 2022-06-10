# Java 手机爬虫

> 使用框架：jsoup



- 爬取网站：https://phonedb.net/
- 介绍：

> ​	多线程手机信息爬虫，默认一个写入文件线程 + 俩个爬取线程，爬取全部大概 5-6小时，通过阻塞队列保证多线程爬取的内容不重复，单个写入文件线程保证写入一致性。
>
> ​	爬取顺序，先爬取所有的分页放入阻塞队列，爬取线程按页爬取，根据每页的手机url，进入手机详情页面，获取全部信息，放入队列给写入线程进行写入，按单个手机的map转json写入，防止按数组写入的手机数据过大产生OOM，通过阻塞队列传递结束信号，停止工作。

- 爬取格式：json数组

> 例：
>
> ```json
> [
>   {
>     "SM-J200M/DS Galaxy J2 Duos LTE LATAM": {
>       "FM Radio Receiver": "FM radio (76-90 MHz) with RDS",
>       "Protection from solid materials": "Yes",
>       "RAM Capacity (converted)": "1 GiB RAM",
>       "Platform": "Android",
>       "CPU": "Samsung Exynos 3 Quad 3475 (Island), 2015, 32 bit, quad-core, 28 nm, ARM Mali-T720 GPU",
>       "Number of Display Scales": "16.8M",
>       "Audio Output": "3.5mm",
>       "Supported Cellular Data Links": "GPRS , EDGE , UMTS , HSUPA , HSUPA 5.8 , HSDPA , HSPA+ 21.1 , HSPA+ 42.2 , LTE , LTE 150/50 data links",
>       "Brief": "The sleekly made Samsung Galaxy J2 fits comfortably in.. \u203a\u203a",
>       "SIM Card Slot": "Micro-SIM (3FF)",
>       "Touchscreen Type": "Capacitive multi-touch screen",
>       "Display Type": "AM-OLED display",
>       "Battery": "Li-ion",
>       "Manufacturer": "Samsung Electronics",
>       "Camera Placement": "Rear",
>       "OEM ID": "J200MZDDZTM",
>       "Operating System": "Google Android 5.1.1 (Lollipop)",
>       "Display Area Utilization": "64.8%",
>       "Camera Image Sensor": "CMOS",
>       "Secondary Camera Placement": "Front",
>       "Display Diagonal": "119.5 mm",
>       "Scratch Resistant Screen": "No",
>       "Added": "2022-05-26 12:18",
>       "Released": "2015 Dec",
>       "Talk Time": "12.0 hours",
>       "Aux. 2 Camera Image Sensor": "No",
>       "Built-in accelerometer": "Yes",
>       "Brand": "Samsung",
>       "Mass": "130 g",
>       "Number of effective pixels": "5.0 MP camera",
>       "Focus": "CD AF",
>       "Nominal Battery Capacity": "2000 mAh battery",
>       "Video Recording": "1280x960 pixel",
>       "Expansion Interfaces": "TransFlash , microSD , microSDHC , microSDXC",
>       "Resolution": "540x960",
>       "Market Regions": "Central America , Southeast Asia",
>       "Secondary Camera Sensor": "CMOS",
>       "Wireless LAN": "802.11b , 802.11g , 802.11n",
>       "Aux. 3 Camera Image Sensor": "No",
>       "Built-in compass": "Yes",
>       "Bluetooth": "Bluetooth 4.1",
>       "Complementary GPS Services": "Simultaneous GPS , A-GPS , Geotagging , QuickGPS",
>       "Sec. Aux. Cam. Image Sensor": "No",
>       "Supported GLONASS protocol(s)": "L1OF",
>       "Pixel Density": "234 PPI",
>       "Horizontal Full Bezel Width": "10.41 mm",
>       "Depth": "8.4 mm",
>       "Sec. Supported Cellular Data Links": "GPRS , EDGE",
>       "Camera Extra Functions": "Macro mode , Panorama Photo , Face detection",
>       "Market Countries": "Argentina , Bolivia , Brazil , Chile , Colombia , Ecuador , Guatemala , Nicaragua , Panama , Paraguay , Peru",
>       "Graphical Controller": "ARM Mali-T720",
>       "Sec. Supported Cellular Networks": "GSM850 , GSM900 , GSM1800 , GSM1900",
>       "Loudspeaker(s)": "mono",
>       "RAM Type": "Yes",
>       "Zoom": "1.0 x optical zoom",
>       "Secondary Camera Number of pixels": "1.9 MP sec. cam",
>       "Height": "136.5 mm",
>       "Flash": "single LED",
>       "Width": "69 mm",
>       "Dual Cellular Network Operation": "Dual standby",
>       "Secondary Aperture (W)": "f/2.40",
>       "CPU Clock": "1300 MHz",
>       "Non-volatile Memory Capacity (converted)": "8 GB ROM",
>       "Non-volatile Memory Interface": "Yes",
>       "Secondary Video Recording": "800x600 pixel",
>       "A/V Out": "No",
>       "Data Integrity": "Preliminary",
>       "USB": "USB 2.0",
>       "Announced": "2015 Sep",
>       "General Extras": "Haptic touch feedback",
>       "Device Category": "Smartphone",
>       "Sec. SIM Card Slot": "Micro-SIM (3FF)",
>       "Additional sensors": "Hall , L sensor , P sensor",
>       "Aperture (W)": "f/2.20",
>       "Estimated Battery Life": "11.0 hours",
>       "Supported Cellular Bands": "GSM850 , GSM900 , GSM1800 , GSM1900 , UMTS2100 (B1) , UMTS1900 (B2) , UMTS1700/2100 (B4) , UMTS850 (B5) , UMTS900 (B8) , LTE2100 (B1) , LTE1900 (B2) , LTE1800 (B3) , LTE1700/2100 (B4) , LTE850 (B5) , LTE2600 (B7) , LTE900 (B8) , LTE700 (B17) , LTE700 (B28) bands",
>       "Complementary Phone Services": "Voice transmission , Voice speaker , Vibrate , Speakerphone",
>       "Hardware Designer": "Samsung Electronics",
>       "Aux. 4 Camera Image Sensor": "No",
>       "Model": "SM-J200M/DS Galaxy J2 Duos LTE LATAM",
>       "Aux. Camera Image Sensor": "No",
>       "Protection from liquids": "Yes",
>       "Microphone(s)": "mono"
>     }
>   }
> ]
> ```

