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
    consumos: consumoDTO[];
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
export interface consumoDTO{

}
export interface ItemConsumoDTO {
    id: number;
    descripcion: string;
    monto: number;
    facturado: boolean;
    responsable: string | null; // DNI del huésped o null
    seleccionado: boolean;
}

export const ITEMS_CONSUMO_MOCK: ItemConsumoDTO[] = [
    { id: 1, descripcion: 'MiniBar - Agua', monto: 1.50, facturado: false, responsable: null,seleccionado:false },
    { id: 2, descripcion: 'MiniBar - Cerveza', monto: 3.00, facturado: false, responsable: null,seleccionado:false },
    { id: 3, descripcion: 'Lavandería - Camisa', monto: 5.00, facturado: false, responsable: null,seleccionado:false },
    { id: 4, descripcion: 'MiniBar - Snacks', monto: 4.00, facturado: false, responsable: null,seleccionado: false},
];