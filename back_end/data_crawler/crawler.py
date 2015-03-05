
import requests
import re, urllib, time, sys, pickle, os
from bs4 import BeautifulSoup
import MySQLdb

import traceback
import datetime

import argparse

SITE_URL = "https://www.manheim.com/"
USERNAME = "5624081194"
PASSWORD = "569444"
SEARCH_URL = "https://www.manheim.com/members/powersearch/basicSearchResults.do"
param = "searchOperation=Refine&sellerCompany=&newSort=false&sortKeys=YEAR&previousSortKeys=&sortIndicator=FORWARD&recordOffset=0&vehicleTypes=-1&vehicleTypes=104000001&vehicleTypes=104000002&vehicleTypes=104000004&vehicleTypes=104000003&fromYear=ALL&toYear=ALL&distance=&distanceUnits=MILES&zipCode=90805&saleDate=&certified=&searchTerms=&mmrRanges=ALL&inventoryChannels=&pickupState=333&refinements=SALE_DATE%7C430916&refinements=%7C&refinements=CONDITION_INFORMATION%7C104617&listingFromTime=&listingToTime=&submittedFilters=&vehicleUniqueId=&detailPageUrl=&vin=&channel=&displayDistance=&saleId=&saleGroupId=&fromOdometer=0&toOdometer=ALL&fromValuation=0&toValuation=ALL&valuationType=MMR&includeMissingValuations=on&conditionGradeRefined=true&fromConditionGrade=1.0&toConditionGrade=5.0&resultsPerPage=250&resultsPerPage=250& =%7C&srpResultsPerPage=250&wbResultsPerPage=25&srpSortKeys=&wbSortKeys=&wtTracker=%28wtSearchType%2CPowerSearch+Advanced%29%28wtRefLinkPrefix%2Cps_srchfm_%29%28wtSavedSearchRefLink%2C%29%28wtSavedSearchTypeLink%2C%29"
CUSTOM_HEADER = {
    'User-Agent': "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36",
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
    'Origin': 'https://www.manheim.com',
    'Content-Type': 'application/x-www-form-urlencoded',
    'Referer': 'https://www.manheim.com/members/powersearch/?WT.svl=m_hdr_mnav_buy_search',
    'Accept-Language': 'en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4,zh-TW;q=0.2'}
CUSTOM_HEADER['User-Agent'] = 'Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36'

COOKIE_FILENAME = "cookies.txt"


class xDB(object):
    """database operations"""
    def __init__(self):
        self.__conn = None
        self.cursor = None
    def connect(self, host="localhost", port=3306,
                user="root", password="", dbname="lxnj"):
        #host, port, user, password, dbname = parseConfig()
        self.__conn = MySQLdb.connect(host=host, port=port,
                                      user=user, passwd=password, db=dbname)
        #self.__conn.autocommit(False)
        self.cursor = self.__conn.cursor()
        self.cursor.execute("SET NAMES UTF8")
    def close(self):
        self.__conn.commit()
        self.cursor.close()
        self.__conn.close()
    def commit(self):
        self.__conn.commit()

    def insertSingleCarInfo(self, car):
        sql = "INSERT INTO car(VehicleId, Lane, Run, " \
              "Year, Make, Model, Abstract, " \
              "`Condition`, `Engine`, Type, " \
              "Miles, Color, VIN, Price, SaleDate) " \
              "VALUES(%s,%s,%s,%s,%s," \
              "%s,%s,%s,%s,%s," \
              "%s,%s,%s,%s,%s)" \
              "ON DUPLICATE KEY UPDATE " \
              "Lane=VALUES(Lane), Run=VALUES(Run), Year=VALUES(Year), Make=VALUES(Make)," \
              "Model=VALUES(Model), Abstract=VALUES(Abstract), `Condition`=VALUES(`Condition`)," \
              "`Engine`=VALUES(`Engine`), Type=VALUES(Type), Miles=VALUES(Miles)," \
              "Color=VALUES(Color), VIN=VALUES(VIN), Price=VALUES(Price), SaleDate=VALUES(SaleDate)"
        try:
            res = self.cursor.execute(sql,
                                  (car["vehicleId"], car["lane"], car["run"],
                                   car["year"], car["make"], car["model"], car["abstract"],
                                   car["condition"], car["engine"], car["type"],
                                   car["miles"], car["color"], car["vin"], car["price"], car["saleDate"]))
            #print 'vehicle done.'
        except Exception, e:
            # print "Failed an item:"
            # print car
            # print sql
            # traceback.print_exc(file=sys.stdout)
            return 0
        #self.__conn.commit()
        #self.cursor.executemany()
        return 1

def save_cookies(requests_cookiejar, filename = COOKIE_FILENAME):
    with open(filename, 'wb') as f:
        pickle.dump(requests_cookiejar, f)

def load_cookies(filename = COOKIE_FILENAME):
    try:
        with open(filename, 'rb') as f:
            return pickle.load(f)
    except:
        return None

def login():
    s = requests.session()
    #s.headers.update(CUSTOM_HEADER)  # Sessions can also be used to provide default data to the request methods.
    r = s.get(SITE_URL, headers = CUSTOM_HEADER)
    pattern = '<input name="authenticity_token" type="hidden" value="(.*?)" />'
    res = re.findall(pattern, r.text)
    if len(res) == 0:
        return False
    payload = "utf8=%E2%9C%93&" + urllib.urlencode({
                    "authenticity_token": res[0],
                    "user[username]": USERNAME,
                    "user[password]": PASSWORD,
                    "remember": "1",
                    "submit": "Login"
                })
    r = s.post("https://www.manheim.com/login/authenticate", data=payload, headers=CUSTOM_HEADER)

    with open('afterlogin.txt', 'w') as f:
        f.write(r.text)
    return s

def isLoggedIn(response):
    #res = re.findall('user\[username\]', response.text)
    if not response or response.status_code != 200:
        return False
    pos = response.url.find(u'www.manheim.com/login')
    if pos == -1:
        return True
    else:
        return False

class SerializedSession(object):
    """store cookies"""
    def __init__(self):
        self.reload_cookies()

    def reload_cookies(self):
        self.session = requests.session()
        cookies = load_cookies()
        if cookies != None:
            self.session.cookies = cookies

    def get(self, URL):
        r = self.session.get(URL, headers=CUSTOM_HEADER)
        if not isLoggedIn(r):
            r = self.noreplay_login('GET', URL)
        return r
    def post(self, URL, data):
        r = self.session.post(URL, data, headers=CUSTOM_HEADER)
        if not isLoggedIn(r):
            r = self.noreplay_login('POST', URL, data)
        return r

    def noreplay_login(self, method, URL, data=None):
        loggingInProgressFilename = "logginginprogress.txt"
        for looper in range(3):
            if not os.path.exists(loggingInProgressFilename):
                open(loggingInProgressFilename, 'a').close()
                # races to the first place
                s = login()
                if str(method).upper() == "GET":
                    r = s.get(URL, headers=CUSTOM_HEADER)
                else:
                    r = s.post(URL, data, headers=CUSTOM_HEADER)
                if isLoggedIn(r):
                    save_cookies(s.cookies)
                    self.session = s
                else:
                    r = None
                try:
                    os.remove(loggingInProgressFilename)
                except:
                    pass
                return r
            else:
                # wait for others to perform log in
                time.sleep(3)
                self.reload_cookies()
                if str(method).upper() == "GET":
                    r = self.session.get(URL, headers=CUSTOM_HEADER)
                else:
                    r = self.session.post(URL, data, headers=CUSTOM_HEADER)
                if isLoggedIn(r):
                    return r
        return None


def parseRow(tr):
    try:
        tds = tr.find_all('td')
        anchor = tds[0].find('a')
        jsparams = re.findall('wtWorkbookAdd\((.*?)\)', anchor.attrs['href'])[0]
        jsparams = map(lambda i: i.strip()[1:-1], jsparams.split(','))

        lane_run = tds[1].getText().strip()  # u'97/45'  or  u'OVE'
        m = lane_run.find('/')
        if m == -1:
            return None
        lane = int(lane_run[:m])
        run = int(lane_run[m+1:])
        crtext = tds[3].find('span', 'cr').getText()  # u'CR 1.0'
        cr = float(crtext.replace('CR', '').strip())
        engine_type = tds[4].getText().strip()  # u'8/A'
        m = engine_type.find('/')
        engine = engine_type[:m]
        etype = engine_type[m+1:]
        mmr = tds[8].getText().strip()  # u'Not Available' : MMR
        if mmr == u'Not Available':
            return None
        mmr = int(mmr.replace('$', '').replace(',', ''))
        saleDate = anchor.attrs['data-sale-date']  # u'01/09/2015'
        saleDate = time.strftime("%Y-%m-%d", time.strptime(saleDate, "%m/%d/%Y")) # to u'2015-01-09'
        return {
            "vehicleId": anchor.attrs['data-unique-id'],  #  u'SIMULCAST.AAAI.11798457' == jsparams[4]
            "lane": lane,
            "run": run,
            "year": int(jsparams[7]),
            "make": jsparams[8],
            "model": jsparams[9],
            "abstract": tds[2].getText().strip(),  # 2015 Chevrolet G2500 CARGO 4X2
            "condition": cr,
            "engine": engine,
            "type": etype,
            "miles": int(tds[5].getText().strip().replace(',', '')),  # u'5,746' Odo, miles
            "color": tds[6].getText().strip(),  # u'White'
            "vin": jsparams[6],  # u'1GCWGFFF1F1103824'
            "price": mmr,
            "saleDate": saleDate,
        }
    except Exception as e:
        # print '=' * 80
        # print tr
        # print '-----------'
        # traceback.print_exc(file=sys.stdout)
        # print '-----------'
        # print e
        # print '==========='
        return None


def parsePage(html_doc, db):
    #Feature List: 1. Ln (Invisible) 2. Run 3. Make 4. Model 5. Condition
    # 6. Engine 7. Type 8. Miles 9. Color 10. Vin 11. Price
    soup = BeautifulSoup(html_doc)
    trs = soup.findAll("tr", "vehicleResultRow")
    count = 0
    score = 0
    for tr in trs:
        #print 'vehicle no.', count
        count += 1
        #print tr
        row = parseRow(tr)
        if row:
            #print row
            score += db.insertSingleCarInfo(row)
    return (score, count)

def fetchPage(session, saleDate, pageno = 1):
    offset = 250 * (pageno - 1)
    #saleDate = "01/07/2015"  #Jan 05, 2015 (1390)
    param = [('searchOperation', 'Paging'),
            ('sellerCompany', ''),
            ('newSort', 'false'),
            ('sortKeys', 'MAKE'),
            ('previousSortKeys', 'YEAR'),
            ('sortIndicator', 'FORWARD'),
            ('recordOffset', offset),
            ('vehicleTypes', '-1'),
            ('vehicleTypes', '104000001'),
            ('vehicleTypes', '104000002'),
            ('vehicleTypes', '104000004'),
            ('vehicleTypes', '104000003'),
            ('fromYear', 'ALL'),
            ('toYear', 'ALL'),
            ('distance', '0'),
            ('distanceUnits', 'MILES'),
            ('zipCode', '90805'),
            ('auctionLocations', '124'),
            ('auctionLocations', '194'),
            ('auctionLocations', '546621'),
            ('inventories', '56'),
            ('saleDate', saleDate),
            ('certified', ''),
            ('searchTerms', ''),
            ('mmrRanges', 'ALL'),
            ('conditionGrades', '8536'),
            ('conditionGrades', '8532'),
            ('conditionGrades', '8534'),
            ('conditionGrades', '8533'),
            ('inventoryChannels', ''),
            ('refinements', 'SALE_TYPE|16230'),
            ('refinements', 'TRANSMISSION|263'),
            ('refinements', 'SALVAGE|50095'),
            #('refinements', 'SALE_DATE|430918'),
            ('refinements', 'TRANSMISSION|50016'),
            ('listingFromTime', ''),
            ('listingToTime', ''),
            ('submittedFilters', ''),
            ('vehicleUniqueId', ''),
            ('detailPageUrl', ''),
            ('vin', ''),
            ('channel', ''),
            ('displayDistance', ''),
            ('saleId', ''),
            ('saleGroupId', ''),
            ('fromOdometer', '0'),
            ('toOdometer', 'ALL'),
            ('fromValuation', '0'),
            ('toValuation', 'ALL'),
            ('valuationType', 'MMR'),
            ('includeMissingValuations', 'on'),
            ('conditionGradeRefined', 'false'),
            ('fromConditionGrade', '0.0'),
            ('toConditionGrade', '5.0'),
            ('resultsPerPage', '250'),
            ('resultsPerPage', '250'),
            ('srpSortKeys', ''),
            ('wbSortKeys', ''),
            ('wbResultsPerPage', '25'),
            ('srpResultsPerPage', '250'),
            ('wtTracker', '(wtSearchType,PowerSearch Advanced)(wtRefLinkPrefix,ps_srchfm_)(wtSavedSearchRefLink,)(wtSavedSearchTypeLink,)'),
            ('searchResultsOffset', offset)]

    r = session.post(SEARCH_URL, data=param)
    filename = 'data_%s_%d.html' % (time.strftime("%Y%m%d", time.strptime(saleDate, "%m/%d/%Y")), pageno)
    with open(filename, 'w') as f:
        f.write(r.text)
    return r.text


def parseNumberOfVehicles(html_doc):
    res = re.findall('(\d+)\s*Vehicles Found', html_doc, re.S)
    if len(res) > 0:
        try:
            ans = int(res[0])
        except ValueError:
            ans = 0
        return ans
    else:
        return None

def crawlDataOfDay(daysFromToday=0, db=None, session=None):
    dbNewlyOpened = False
    if not db:
        db = xDB()
        db.connect()
        dbNewlyOpened = True
    if not session:
        session = SerializedSession()
    saleDate = time.strftime("%m/%d/%Y", time.localtime())  #"01/07/2015"
    targetDate = datetime.date.today() + datetime.timedelta(days=daysFromToday)
    saleDate = targetDate.strftime("%m/%d/%Y")  #"01/07/2015"

    pageno = 1
    while True:
        html_doc = fetchPage(session, saleDate, pageno)
        if pageno == 1:
            npages = (parseNumberOfVehicles(html_doc) - 1) / 250 + 1
            print "Total %d pages for %s." % (npages, saleDate)
        print "Processing page %d:" % pageno
        numOfInsertedItems, count = parsePage(html_doc, db)
        print "Page %d complete! Inserted %d item(s) of all %d item(s)." % (pageno, numOfInsertedItems, count)
        pageno += 1
        if pageno > npages:
            break

    if dbNewlyOpened:
        db.close()

def crawlDays(listOfDays):
    db = xDB()
    db.connect()
    session = SerializedSession()
    print "Login success!"
    for d in listOfDays:
        try:
            daysFromToday = int(d)
        except ValueError, e:
            print e
            continue
        crawlDataOfDay(daysFromToday, db, session)
        db.commit()
    db.close()


if __name__ == "__main__":
    # testParsePage()
    parser = argparse.ArgumentParser(description='Crawler for Manheim.com')
    parser.add_argument('-d', action="append", dest="daysList", default=[],
                        help="Add daysFromToday values to task queue (repeatedly)")
    opts = parser.parse_args(sys.argv[1:])
    if len(opts.daysList) != 0:
        crawlDays(opts.daysList)
