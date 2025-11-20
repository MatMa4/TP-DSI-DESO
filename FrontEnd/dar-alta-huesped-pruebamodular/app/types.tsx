
// Define la estructura de tus datos para que todos los archivos "hablen el mismo idiomaaa".

export interface Direccion {
  calle: string;
  numero: string;
  departamento: string;
  piso: string;
  codigo: string;
  localidad: string;
  provincia: string;
  pais: string;
}

export interface FormData {
  numeroDocumento: string;
  tipoDocumento: string;
  apellido: string;
  nombre: string;
  fechaNacimiento: string;
  telefono: string;
  email: string;
  ocupacion: string;
  nacionalidad: string;
  cuit: string;
  posicionIVA: string;
  alojado: boolean;
  direccionHuesped: Direccion;
}