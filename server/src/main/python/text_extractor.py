# -*- coding: utf-8 -*-

import requests
import feedparser
import requests
from bs4 import BeautifulSoup

class News:
   def __init__(self, title, description, text):
     self.description = description
     self.title = title
     self.text = text

def appendAllContent(paragraps) :
  content = ""
  
  for par in paragraps:
    content = content + par.get_text()
    content = content + ' '
  return content 

def parse_news(url, title) :
  site_news = []

  response = requests.get(url).content
  soup = BeautifulSoup (response, 'html.parser')

  body = soup.find('body')

  wraper = body.find('div', {"class" : "wraper"})
  
  story_desc = wraper.find("div", {"class": "article__description"})
  story_text = wraper.find("div", {"class": "article__body"})
  
  desc = ''
  if story_desc is not None:
    desc = appendAllContent(story_desc.find_all('p'))

  story = ''
  if story_text is not None:
    story = appendAllContent(story_text.find_all('p'))

  news = News(title, desc, story)

  print(news.text)

sites = ['https://www.mk.ru/rss/science/index.xml']

def get_site(site) :
  feed = feedparser.parse(site)

  for article in feed['entries']:
    parse_news(article['link'], article['title'])

for site in sites:
  get_site(site)