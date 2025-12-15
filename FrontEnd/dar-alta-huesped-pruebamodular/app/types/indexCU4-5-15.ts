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

export interface RoomCellData {
    roomId: string;
    roomType: string;
    date: string; 
    estado: 'Disponible'| 'Fuera de servicio'|'Reservada'|'Ocupada';
    reservationId?: string; 
    reservedBy?: string; 
    reservedDNI?: string;
}

export interface SelectedReservation {
    fechaInicio: string;
    fechaFin: string;
    roomId: string;
    type: string; 
}

export interface EventualHuesped {
    nombre: string;
    apellido: string;
    telefono: string;
}

export const DNI_TYPES_OPTIONS: Array<'DNI' | 'LC' | 'Pasaporte'> = [
    'DNI',
    'LC',
    'Pasaporte',
];

export const ROOM_TYPES = ['Individual Estándar', 'Doble Estándar', 'Doble Superior', 'Superior Family Plan', 'Suite'];


export interface RoomDetail {
    numero: number;
    costoPorNoche: number;
    capacidad: number;
    estado: string; 
    tipoHabitacion: string; 
    descripcion: null;
    camaDoble: number,
}

export interface OcupacionDetail {
    fechaInicio: string; 
    fechaFin: string;   
}

export interface RoomStatusDTO {
    habitacion: RoomDetail;
    ocupaciones: OcupacionDetail[];
    reservas: any[];
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
export interface OcupacionDTO {
    id: number;
    habitacion: HabitacionDTO; 
    fechaInicio: string; 
    fechaFin: string; 
    checkIn: string,
    checkOut: string,
    consumos: consumoDTO[];
    huespedes: HuespedDTO[]; 
    precioTotal: number;
}
export interface HuespedFacturacionDTO {
    nombre: string;
    apellido: string;
    dni: string;
    posicionIVA: string;
}

export interface consumoDTO{

}
