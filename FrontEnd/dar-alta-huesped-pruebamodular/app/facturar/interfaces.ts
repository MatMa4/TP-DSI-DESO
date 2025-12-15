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
export interface OcupacionDTO {
    id: number;
    habitacion: HabitacionDTO; 
    fechaInicio: string; 
    fechaFin: string; 
    checkIn: string,
    checkOut: string,
    consumos: ItemConsumoDTO[];
    huespedes: HuespedDTO[]; 
    precioTotal: number;
}
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
export interface HabitacionDTO {
    numero: number;
    costoPorNoche: number;
    capacidad: number;
    estado: string; 
    descripcion: null;
    camasIndividuales: number;
    camaDoble: number;
    camaKingsize: number;
    tipoHabitacion: string;
}
export interface ItemConsumoDTO {
    idConsumo: number;
    tipoServicio: string;
    detalle: string;
    monto: number;
    facturado: boolean;
}

export interface PersonaJuridicaDTO {
    id: number;          
    razonSocial: string; 
    cuit: string;        
    telefono: string;
    direccion: Direccion;
}