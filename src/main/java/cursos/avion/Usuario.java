package cursos.avion;

public class Usuario {
    private int id;
    private String nombre;
    private String apellidos;
    private String correo;
    private String ident;
    private String nombreUsuario;
    private String contraseña;
    private String tipo;

    // Constructor para crear un usuario nuevo (sin id, ya que será generado por la base de datos)
    public Usuario(String nombre, String apellidos, String correo, String ident, String nombreUsuario, String contraseña, String tipo) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.ident = ident;
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
        this.tipo = tipo;
    }

    // Constructor para cuando recuperamos un usuario de la base de datos (incluye id)
    public Usuario(int id, String nombre, String apellidos, String correo, String ident, String nombreUsuario, String contraseña, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.ident = ident;
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
        this.tipo = tipo;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getIdent() { return ident; }
    public void setIdent(String ident) { this.ident = ident; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}