from urllib import request

ids = {
    '296b6f7c-4429-4a32-bbfe-a6f2e0a6a762-1',
    'ec6d5101-9392-4288-bdd0-fca912a4f2c5-1',
    'a640d366-f792-4a1e-83bc-922fb122d8ff-1',
    'a6d8e7cc-05a8-4808-a0f2-6720f5ef9263-1',
    'e69d7fa8-300e-4731-b810-73792b367b7c-1',
}

with open('urls.txt') as urls:
    for url_count_candidate_id in urls:
        url = url_count_candidate_id.split(',')[0]
        segment_url = url.replace('manifest.m3u8', '480p_000.ts')
        id = url.split('/')[4]
        if id in ids:
            file_name = f'original/480p_original_{id}.ts'
            request.urlretrieve(segment_url, file_name) 