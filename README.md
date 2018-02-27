# Files
Filtering and Showing files based in some criterias.

## Techs:
Maven , groovy and Spock.

## Enunciado
  
En un directorio tenemos un montón de archivos, y nuestro objetivo es poder catalogar los archivos de a pares.

Consideramos dos imagenes como un par cuando
* Una de ellas se llama `left_xxx.png`, donde `xxx` es un entero.
* La otra se llama `right_xxx.png`, donde `xxx` es un entero.
* Los numeros de cada imagen (`xxx`) coinciden. Por ejemplo `left_1.png` y `left_2.png` forman un par.
* Las dimensiones de las imagenes son las mismas (width y height).
* Si esto no ocurre consideramos al par "fallado".
* Hay que considerar que si las imagenes están corruptas, tal vez no podamos leer las dimensiones. En ese caso reportamos ambos archivos como "fallados" (los dos, no solo el que no se pudo leer).
* En el caso de que un par sea "fallado", se tiene que saber tambien la razon por la que no se pudo crear el par.

Además de archivos que cumplan con este pattern, en el directorio pueden existir tambien otros tipos de archivos. Por ejemplo:
* `left_a.png` (la parte del número no es un entero).
* `foo_1.png` (no es ni `left` ni `right`).
* `left_1.txt` (no es una imagen).
* `foo.txt`
* etc

## Objetivo
Dado un directorio con, por ejemplo, los siguientes archivos:

* `left_1.png`  (100x100)
* `left_2.png`  (100x100)
* `left_3.png`  (100x100)
* `left_4.png`  (100x100)
* `right_1.png` (100x100)
* `right_2.png` (200x200)
* `right_3.png` no se puede leer, imagen corrupta
* `left_a.png`
* `foo_1.png`
* `left_1.txt
* `foo.txt`

retornar un diccionaro en el que se cataloguen los archivos por pares, pares fallados, archivos huerfanos, y archivos ignorados.
Para el ejemplo anterior el resultado sería:
```
{
    pairs: [{left: left_1.png right: right_1.png}]
    failed_pairs: [
      {error: 'size mismatch', left: left_2.png right: right_2.png},
      {error: 'cannor read right_3.png', left: left_3.png right: right_3.png}
    ],
    orphans: [left_4.png],
    ignored: [
      left_a.png,
      foo_1.png,
      left_1.txt,
      foo.txt
    ]
}
```

## Notas
* El objetivo es catalogar los archivos. No importa que tan pesados sean ni como se van a procesar esos archivos después de ser catalogados.
* El file system es local (no es un file system remoto, no hay fallos, reintentos, etc).
* Puede ocurrir que al ejecutar el programa el archivo `left_1.png` esté en el directorio y el archivo `right_1.png` está en proceso de escritura. En este caso `left_1.png` se lo va a catalogar como huerfano. En una corrida posterior del programa se los considerará un par, si se cumplen todas las condiciones. 


