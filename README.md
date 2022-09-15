# Parcial_CVDS
## 1. GIT
  ### - Fork
  - El repositorio que vamos a trabajar se encuentra en el siguiente link de [GITHUB](https://github.com/DanielOchoa1214/Tetris.git)
  - Para poder trabajar con este repositorio debemos hacer un fork, ya que el repositorio original no podemos modificar.
  
  ![image](https://user-images.githubusercontent.com/77862048/190240776-5ce0bca6-715d-4b50-a72f-7af4e17f5f41.png)

  - Como podemos observar nos aparece la opción de hacer fork desde el repositorio original.
  
  ![image](https://user-images.githubusercontent.com/77862048/190241172-c1faa7dc-95b0-4412-8896-d627ff26afc1.png)

  - En esta imagen se puede observar que un nuevo repositorio se creo en el GITHUB donde trabajaremos.
  
  ![image](https://user-images.githubusercontent.com/77862048/190241397-fb33fa96-7b25-4cb4-a15f-89fc12213a2f.png)

  - Ademas podemos observar desde el repositorio de nuestro GITHUB desde donde se hizo el fork.
  ### - Clone
  - Despues de hacer el fork debemos clonar el repositorio remoto al local.
  
  ![image](https://user-images.githubusercontent.com/77862048/190242380-0f746e2d-2b9c-4bb0-9314-64803b45f8dc.png)

  - Como vemos en la imagen hacemos clone desde el repositorio remoto y lo ponemos en la carpeta "ParcialTercioUno"
  
## 2. Identificando malas practicas
  ### - SOLID
   - Mirando los códigos de la capa de dominio pudimos observar un incupmlimiento al principio de SOLID especificamente a la L.
   - 
      ![image](https://user-images.githubusercontent.com/77862048/190265165-4f2b0a7c-753f-43e8-8e74-5ad6c0ac298d.png)
      
   - Mirando el diagrama de clases podemos observar que Board posee dos clases hijas. La clase SlowBoard practicamente no posee logica segun este diagrama.
   - 
      ![image](https://user-images.githubusercontent.com/77862048/190265318-ddc6a0ea-38e5-42d9-98e0-e9e035e3ba35.png)
      
   - Esto se ve reflejado en el código donde solo vemos al constructor de la clase.
     * El funcionamiento de cada clase es el siguiente:
        1. La clase Board es una clase abstracta que posee la lógica principal para el movimiento de las fichas.
        2. La clase AcceleratedBoard debe hacer que las fichas caigan mas rapido despues de cierto tiempo
        3. La clase SlowBoard se caracteriza por tener la velocidad constante
   - Teniendo en cuenta el funcionamiento de cada clase pudimos observar que el método "resume()" tenia una particularidad.

      ![image](https://user-images.githubusercontent.com/77862048/190266129-0a65d406-ca27-4001-a934-570cea30ff67.png)
      
      ![image](https://user-images.githubusercontent.com/77862048/190266821-1c3209b4-3f33-47c9-a108-c899da989523.png)

   - La varible fallingVelocity no tiene sentido en el caso que el tablero sea SlowBorad, eso quiere decir que la logica de este metodo no deberia estar en la clase padre por cuestiones de funcionamiento ya descritos. Esto se resolveria haciendo que cada clase hija haga su propia logica que cumpla con sus funcionalidades.
      
#### SlowBoard

```
  public class SlowBoard extends Board implements Serializable {
      /**
       * Constructor de los tableros lentos
       */
      public SlowBoard(){
          spawnPiece();
      }
      
      public void resume(){
        TimerTask increasePoints = new TimerTask() {
            @Override
            public void run() {
                points++;
            }
        };
        TimerTask pushDown = new TimerTask(){
            @Override
            public void run() {
                try{
                    movePieceDown();
                } catch (Exception ignored){}
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(increasePoints, 0, 1000);
        timer.scheduleAtFixedRate(pushDown, 0, 1200L);
        paused = false;
    }
```

#### AcceleratedBoard
```
    public void resume(){
        TimerTask increasePoints = new TimerTask() {
            @Override
            public void run() {
                points++;
            }
        };
        TimerTask pushDown = new TimerTask(){
            @Override
            public void run() {
                try{
                    movePieceDown();
                } catch (Exception ignored){}
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(increasePoints, 0, 1000);
        timer.scheduleAtFixedRate(pushDown, 0, 1200L/fallingVelocity);
        paused = false;
    }
    private void speedUp(){
          TimerTask increaseVelocity = new TimerTask() {
              @Override
              public void run() {
                  if(!isPaused()){
                      pause();
                      currentVelocity++;
                      setFallingVelocity(currentVelocity);
                      AcceleratedBoard.super.resume();
                  }
              }
          };
          timer = new Timer();
          timer.scheduleAtFixedRate(increaseVelocity, 0, 10000);
    }
```

  ###  -  Patterns
  - Propusimos como patron creacional la implementación de una fabrica, ya que en una parte concreta del código pudimos observar esto:
  
  ![image](https://user-images.githubusercontent.com/77862048/190285684-8ddc9e6e-d6da-4bcb-8e62-5ae63772ab78.png)

  - Como vemos en los atributos, algunos crean instancias de clases lo que hace que se genere dependencias y el código se acople.
  - Para solucionar esto proponemos construir una Factory que encarge de la creación de estas instancias y las injecte a la clase que las necesita.

#### Clase abstracta que asegura la creación de estos métodos
```
abstract public class TetrisExpertPlayerFactoryMethod {
  abstract public HashMap<Integer, Integer> createPossiblePositions();
  abstract public Timer createTimer();
  abstract public Map<Integer, Integer> createRotations();
}
```

#### Clase que hereda que usa la clase abstracta para implementar los métodos y crear las instancias.

```
public class HangmanDefaultFactoryMethod extends HangmanFactoryMethod {
    @Override
    public HashMap<Integer, Integer> createPossiblePositions() {
        return new HashMap<>();
    }

    @Override
    public Timer createTimer() {
        return new Timer();
    }

    @Override
    public Map<Integer, Integer> createRotations() {
        return new HashMap<>();
    }
}

```
#### Creación de un nuevo constructor que reciba a la clase Factory como parametro

```
// Use Factory method
  public Expert(String type, TetrisExpertPlayerFactoryMethod factoryMethod) {
      setBoard(type);
      board = getBoardObject();
      this.language = factoryMethod.createPossiblePositions();
      this.dictionary = factoryMethod.createTimer();
      this.hangmanPanel = factoryMethod.createRotations();
  }
```

### - Unit Test
  - Despues de haber visto las fuentes de los test, pudimos ver lo siguiente:
  
  ![image](https://user-images.githubusercontent.com/77862048/190246931-35711e5a-9379-42a1-8717-3b50eaf0a5df.png)

  - Se puede observar que:
      1. No cumple con el nombramiento de las pruebas unitarias
      2. No cumple con el AAA
      3. No cumple con el principio FIRST.
  - Para solucionar esta mala practica deberiamos hacer lo siguiente:
      - Las clases de equivalencia que vemos en este test serian:
          - Limite inferior = Null
          - Nombre Valido = esta entre {"", "Gerber", "SuperJose", "Dano", "ManlyManChad", "GirlyGirlGina"}
          - Nombre Invalido = cualquier nombre que no este dentro de la lista ya dicha
  ```
  
    @Test
    public void give_unUsuario_when_esteSeaUnUsuarioExistenteGerber_then_colorDelUsuarioEsRojo() {
        //Arrange
        TetrisGUI.User = Null;
        try {
            //Act
            player = new HumanPlayer("dominio.SlowBoard");
            fail("No lanzo la excepcion")
        catch (TetrisException e) {
            //Assert
            assertEquals(e.getMessage(), TetrisException.NULL_USER);
        }
    }
    
    @Test
    public void give_unUsuario_when_esteSeaUnUsuarioExistenteGerber_then_colorDelUsuarioEsRojo() {
        //Arrange
        TetrisGUI.User = "Gerber";
        //Act
        player = new HumanPlayer("dominio.SlowBoard");
        //Assert
        assertEquals(player.getBackgroundColor(), new Color(153, 0, 0));
    }
    
    @Test
    public void give_unUsuario_when_esteSeaUnUsuarioExistenteGerber_then_colorDelUsuarioEsRojo() {
        //Arrange
        TetrisGUI.User = "Federico";
        try {
            //Act
            player = new HumanPlayer("dominio.SlowBoard");
            fail("No lanzo la excepcion")
        catch (TetrisException e) {
            //Assert
            assertEquals(e.getMessage(), TetrisException.INVALID_USER);
        }
    }
    
  ```
  ## - GIT (add, commit, push)
  - Despues de haber hecho los cambios propuestos debemos hacer commit del trabajo hecho al repositorio remoto.
    1. Se agregan todas las modificaciones hechas con add
    
    ![image](https://user-images.githubusercontent.com/77862048/190288003-ac414eb5-54d0-4e5f-b3e4-a5ae4ea82be8.png)
    
    2. Posterior hacemos un git status para corroborar los cambios
    
    ![image](https://user-images.githubusercontent.com/77862048/190288065-12cca69f-2532-4aa6-ae90-898d002aa388.png)

    3. Luego de corroborar los cambios, hacemos commit del trabajo y le agregamos un comentario.
    
    ![image](https://user-images.githubusercontent.com/77862048/190288168-c91c08e9-d5ca-4df3-8396-65d68bee8373.png)
    
    4. Luego de haber hecho el commit, queremos que el repositorio local quede en el repositorio remoto para esto hacemos push.
    
    ![image](https://user-images.githubusercontent.com/77862048/190288407-ccc43b86-5d44-4542-8edd-017723d2e0f2.png)

    


   
