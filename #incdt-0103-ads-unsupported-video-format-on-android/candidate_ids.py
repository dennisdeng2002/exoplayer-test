import csv
import re

candidate_ids = []

with open('sentry.csv', newline='') as errors:
    for error in errors:
        video_url = re.findall('video_url = (.*)', error)
        if video_url:
            v = video_url[0]
            candidate_id = re.findall('candidate_id = (.*),',error)
            candidate_ids.append(candidate_id[0])

with open('candidate_ids_1.txt', 'w') as f:
    for candidate_id in candidate_ids[0:15000]:
        f.write(f'\'{candidate_id}\',\n')

with open('candidate_ids_2.txt', 'w') as f:
    for candidate_id in candidate_ids[15000:]:
        f.write(f'\'{candidate_id}\',\n')