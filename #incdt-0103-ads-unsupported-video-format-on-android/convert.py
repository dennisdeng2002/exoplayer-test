from pathlib import Path
import subprocess

for file_name in Path('original').glob("*.ts"):
    commands = [
        'ffmpeg',
        '-i',
        file_name,
        '-pix_fmt',
        'yuvj420p',
        '-c:v',
        'libx264',
        '-profile:v',
        'high',
        str(file_name).replace('original', 'encoded'),
    ]

    subprocess.call(commands)