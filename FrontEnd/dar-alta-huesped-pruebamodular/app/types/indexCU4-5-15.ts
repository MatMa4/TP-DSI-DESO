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
    estado: 'Disponible' | 'Reservada' | 'Ocupada' | 'Fuera de servicio';
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

export const ROOM_TYPES = ['Individual Estándar', 'Doble Estándar', 'Doble Superior', 'Superior Family Plan', 'Suite Doble'];
export const ROOMS =['A101', 'A102', 'B201', 'B202', 'C301', 'C302', 'D401', 'D402', 'S501'];
