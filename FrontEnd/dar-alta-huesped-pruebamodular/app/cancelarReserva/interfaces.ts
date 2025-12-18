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
export interface HuespedDTO { 
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
export interface CriterioBusquedaReserva {
    apellido: string;
    nombre: string;
}
export interface ReservaDTO{
idReserva: number;
fechaInicio: string; 
fechaFin: string;
estado: string;
apellido: string;
nombre: string;
telefono: string;
habitacionNumero:number;
tipoHabitacion:string;
}
