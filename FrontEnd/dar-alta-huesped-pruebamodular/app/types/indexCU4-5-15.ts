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

export interface HuespedDTOCompleto { 
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
    estado: string; // "HABITABLE"
    tipoHabitacion: string; // "IndividualEstándar"
    // ... otros detalles de cama
}

export interface OcupacionDetail {
    fechaInicio: string; // Ej: "2025-12-01T03:00:00.000+00:00"
    fechaFin: string;   // Ej: "2025-12-05T03:00:00.000+00:00"
    // ... otros detalles de ocupación
}

// Interfaz para la respuesta del Back-End (GET /habitaciones)
export interface RoomStatusDTO {
    habitacion: RoomDetail;
    ocupaciones: OcupacionDetail[];
    reservas: any[]; // Usamos 'any' si la estructura es desconocida o compleja
}