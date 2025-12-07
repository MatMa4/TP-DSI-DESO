// --- Archivo: my-hotel-app/types/index.ts ---

// ===============================================
// TIPOS PARA CU09: DAR ALTA HUÉSPED
// ===============================================

/**
 * Define la estructura completa de la dirección del huésped.
 * Usada en el componente DireccionHuesped.
 */
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

/**
 * Define la estructura completa de los datos del huésped.
 * Usada en el componente principal de Dar Alta Huésped.
 */
export interface FormData {
  numeroDocumento: string;
  tipoDocumento: string; // DNI, LE, LC, Pasaporte, Otro [cite: 232, 451]
  apellido: string;
  nombre: string;
  fechaNacimiento: string;
  telefono: string;
  email: string;
  ocupacion: string;
  nacionalidad: string;
  cuit: string; // no obligatorio [cite: 453]
  posicionIVA: string;// Consumidor final por omisión [cite: 455]
  alojado: boolean;
  direccionHuesped: Direccion;
}


// ===============================================
// TIPOS PARA CU04: RESERVAR HABITACIÓN
// ===============================================

/**
 * Define la estructura de una celda en la grilla de disponibilidad (un día por habitación).
 */
export interface RoomCellData {
  roomId: string;
  roomType: string;
  date: string; 
  estado: 'Disponible' | 'Reservada' | 'Ocupada' | 'Fuera de servicio';
  reservationId?: string; 
  reservedBy?: string; 
  reservedDNI?: string;
}

/**
 * Define la información de una reserva seleccionada para la verificación.
 */
export interface SelectedReservation {
  roomId: string;
  type: string;
  fechaInicio: string;
  fechaFin: string;
}

/**
 * Define los campos requeridos para el Huésped Eventual (Reserva).
 * [cite_start]Campos obligatorios: Apellido, Nombre, Teléfono [cite: 253, 254, 255]
 */
export interface EventualHuesped {
    nombre: string;
    apellido: string;
    telefono: string;
}

// ===============================================
// DATOS Y LÓGICA SIMULADA PARA CU04
// (Mantenidos aquí para facilitar la simulación)
// ===============================================
// --- Archivo: my-hotel-app/types/index.ts (Fragmento) ---

// (Asegúrate de que estas constantes estén definidas en este archivo)
export const ROOM_TYPES = ['Individual Estándar', 'Doble Estándar', 'Doble Superior', 'Superior Family Plan', 'Suite Doble'];
export const ROOMS =['A101', 'A102', 'B201', 'B202', 'C301', 'C302', 'D401', 'D402', 'S501'];

/**
 * Genera datos simulados de disponibilidad para la grilla,
 * filtrando por el tipo de habitación seleccionado.
 * * @param desde Fecha de inicio (YYYY-MM-DD)
 * @param hasta Fecha de fin (YYYY-MM-DD)
 * @param selectedType Tipo de habitación para filtrar
 * @returns Array de RoomCellData simulada
 */
export const generateGridData = (desde: string, hasta: string, selectedType: string): RoomCellData[] => {
    
    const start = new Date(desde);
    const end = new Date(hasta);
    const data: RoomCellData[] = [];
    
    // Función auxiliar para obtener todas las fechas en el rango
    const getDateArray = (start: Date, end: Date) => {
        const arr = [];
        let dt = new Date(start);
        while (dt <= end) {
            // Aseguramos que la hora sea medianoche para evitar problemas de zona horaria
            arr.push(new Date(dt)); 
            dt.setDate(dt.getDate() + 1);
        }
        return arr;
    }

    const dateRange = getDateArray(start, end);

    // Asignación de tipos de habitación a las IDs simuladas para simular el filtro
    const ROOM_MAP: { [key: string]: string } = {
        'A101': 'Individual Estándar',
        'A102': 'Individual Estándar',
        'B201': 'Doble Estándar',
        'B202': 'Doble Estándar',
        'S301': 'Suite Doble',
        'C301': 'Doble Superior',
        'C302': 'Doble Superior',
        'D401': 'Superior Family Plan', 
        'D402': 'Superior Family Plan', 
        'S501': 'Suite Doble',
        // Puedes extender esto si tienes más IDs de habitación simuladas
    };

    let cellId = 0; // Usado para simular estados aleatorios

    for (const date of dateRange) {
        const dateString = date.toISOString().split('T')[0];

        for (const [roomId, roomType] of Object.entries(ROOM_MAP)) {
            
            // 1. FILTRO CLAVE: Solo incluir datos si coinciden con el tipo seleccionado
            if (roomType !== selectedType) {
                continue; 
            }

            let estado: RoomCellData['estado'] = 'Disponible';
            
            if (cellId % 5 === 0) {
                estado = 'Reservada';       
                data.push({
                    roomId: roomId,
                    roomType: roomType,
                    date: dateString,
                    estado: estado,
                    reservedBy: `Huésped MOCK ${roomId}`, // Datos simulados por habitación
                    reservedDNI: `20.${Math.floor(Math.random() * 899) + 100}.456`,
                });
            } else {
                if (cellId % 5 === 0) estado = 'Reservada';       // Prueba de advertencia
                if (cellId % 11 === 0) estado = 'Fuera de servicio'; // Prueba de error
            }
            cellId++;
        }
    }
    
    return data; 
};