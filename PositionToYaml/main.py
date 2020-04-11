from webdriver_manager.chrome import ChromeDriverManager
from selenium import webdriver
browser = webdriver.Chrome(ChromeDriverManager().install())
import time


def getCoords(browser, latitudeDegs, latitudeMins, longitudeDegs, longitudeMins):
    selectBPT = '//*[@id="scale"]/option[2]'
    browser.find_element_by_xpath(selectBPT).click()
    latitudeDegsPath = '//*[@id="latitude_hour"]'
    latitudeMinsPath = '//*[@id="latitude_minute"]'
    longitudeDegsPath = '//*[@id="longitude_hour"]'
    longitudeMinsPath = '//*[@id="longitude_minute"]'
    ltd = browser.find_element_by_xpath(latitudeDegsPath)
    ltm = browser.find_element_by_xpath(latitudeMinsPath)
    lgd = browser.find_element_by_xpath(longitudeDegsPath)
    lgm = browser.find_element_by_xpath(longitudeMinsPath)
    ltd.clear()
    ltm.clear()
    lgd.clear()
    lgm.clear()
    ltd.send_keys(latitudeDegs)
    ltm.send_keys(latitudeMins)
    lgd.send_keys(longitudeDegs)
    lgm.send_keys(longitudeMins + "\n")
    xcoordXpath = '//*[@id="xcoord"]'
    zcoordXpath = '//*[@id="zcoord"]'
    time.sleep(0.5)
    x = browser.find_element_by_xpath(xcoordXpath).get_attribute("value")
    z = browser.find_element_by_xpath(zcoordXpath).get_attribute("value")
    return (x, z)


browser.get("https://earth.motfe.net/coords/CoordinateCalculator.html")
getCoords(browser, '34', '28', '69', '11')

with open("file.html", "r") as f:
    text = f.read()


with open("config.yml", "w") as cfg:
    cfg.write("cooldown: 3\ndelay: 5\ncountries: \n")
    for i in text.split('\n \n'):

        coord1 = i.split("\n")[2][4:-5]
        if coord1.endswith("S"):
            coord1 = "-" + coord1[:-1] + "N"
        coord2 = i.split("\n")[3][4:-10]
        if coord2.endswith("W"):
            coord2 = "-" + coord2[:-1] + "E"
        latitudeDegs = coord1.split("&deg;")[0]
        latitudeMins = coord1.split("&deg;")[1][:-2]
        longitudeDegs = coord2.split("&deg;")[0]
        longitudeMins = coord2.split("&deg;")[1][:-2]

        x, z = getCoords(browser, latitudeDegs, latitudeMins, longitudeDegs, longitudeMins)

        name = i.split('\n')[0][8:][:-5]
        name = name.lower().replace(" ", "_")
        capital = i.split('\n')[1][4:][:-5]
        x = str(x)
        z = str(z)
        cfg.write("  " + name + ":\n    capital: " + capital +
                  "\n    coords:\n      - " + str(x) + "\n      - " + "255"
                  + "\n      - " + str(z) + "\n")
