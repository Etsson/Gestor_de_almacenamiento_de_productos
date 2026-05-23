import urllib.request
import os

url = "https://github.com/xerial/sqlite-jdbc/releases/download/3.44.0.0/sqlite-jdbc-3.44.0.0.jar"
output = r"lib\sqlite-jdbc-3.44.0.0.jar"

print(f"Descargando {url}...")

try:
    urllib.request.urlretrieve(url, output)
    print(f"✓ Descargado correctamente en: {output}")
    print(f"  Tamaño: {os.path.getsize(output)} bytes")
except Exception as e:
    print(f"✗ Error: {e}")
