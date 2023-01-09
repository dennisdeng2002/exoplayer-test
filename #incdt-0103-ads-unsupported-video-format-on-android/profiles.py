from pathlib import Path
import subprocess

f = open('profiles.txt', 'w')
for file_name in Path('original').glob("*.ts"):
    commands = [
        'ffprobe',
        '-v',
        'error',
        '-select_streams',
        'v:0',
        '-show_entries',
        'stream=profile',
        '-of',
        'default=noprint_wrappers=1:nokey=1',
        file_name,
    ]

    subprocess.call(commands, stdout=f)
