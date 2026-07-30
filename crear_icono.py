from PIL import Image, ImageOps

fondo = Image.open("fondo.jpg")
icono = Image.open("icono.png")

fondo = fondo.resize((1024, 1024))
icono = icono.resize((1024, 1024))

mascara = icono.convert("L")
mascara = mascara.point(lambda p: 255 if p > 128 else 0)

resultado = Image.new("RGBA", (1024, 1024))
resultado.paste(fondo, (0, 0), mask=mascara)

resultado.save("icono_final.png")
print("✅ icono_final.png creado")
