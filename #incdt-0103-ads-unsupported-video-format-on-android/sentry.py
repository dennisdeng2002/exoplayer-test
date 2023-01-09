import csv
import re

video_urls = {}
candidate_ids = {}

with open('sentry.csv', newline='') as errors:
    for error in errors:
        video_url = re.findall('video_url = (.*)', error)
        if video_url:
            v = video_url[0]
            video_urls[v] = video_urls.get(v, 0) + 1
            candidate_id = re.findall('candidate_id = (.*),',error)
            candidate_ids[v] = candidate_id[0]

with open('urls.txt', 'w') as f:
    for (video_url, count) in video_urls.items():
        f.write(f'{video_url}, {count}, {candidate_ids[video_url]}\n')