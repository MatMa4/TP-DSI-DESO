// Asumo que estas interfaces están definidas en tu archivo de tipos
import { RoomStatusDTO, RoomCellData, ROOM_TYPES } from '../types/indexCU4-5-15';

// [getDatesInRange] y [formatToYMD] se mantienen sin cambios (asumiendo que funcionan)

const formatToYMD = (dateStr: string): string => {
    // Usamos el constructor de Date para manejar ISO y extraemos solo la fecha.
    const date = new Date(dateStr);
    const timezoneOffset = date.getTimezoneOffset() * 60000;
    const correctedDate = new Date(date.getTime() + timezoneOffset);
    return correctedDate.toISOString().split('T')[0];
};

const getDatesInRange = (startStr: string, endStr: string): string[] => {
    const dates = [];
    // Aseguramos que la fecha se interprete correctamente con YYYY-MM-DD
    const currentDate = new Date(startStr + 'T00:00:00'); 
    const endDate = new Date(endStr + 'T00:00:00');
    
    // Si hay un error, el loop no iniciará. 
    if (isNaN(currentDate.getTime()) || isNaN(endDate.getTime())) return [];

    while (currentDate <= endDate) {
        dates.push(formatToYMD(currentDate.toISOString()));
        currentDate.setDate(currentDate.getDate() + 1);
    }
    return dates;
};


export const transformToGridData = (
    rawData: RoomStatusDTO[],
    fechaInicio: string,
    fechaFin: string,
    targetRoomType: string
    
): RoomCellData[] => {

    const normalizedTargetType = targetRoomType.replace(/\s/g, '');
    const allDates = getDatesInRange(fechaInicio, fechaFin);
    console.log("Rango de fechas generado:", allDates);
    const gridData: RoomCellData[] = [];

    // 1. Filtrar las habitaciones por tipo
    const filteredRooms = rawData.filter(roomDto => {
        // Normalizar el tipo recibido del Back-End (eliminar espacios)
        const normalizedBackendType = roomDto.habitacion.tipoHabitacion.replace(/\s/g, '');
        
        // CRÍTICO: Comparar las cadenas normalizadas
        return normalizedBackendType === normalizedTargetType;
    });
    console.log("Filtered Rooms Count:", filteredRooms.length);


    // 2. Iterar sobre cada habitación y cada día dentro del rango
    for (const roomDto of filteredRooms) {
        const roomId = roomDto.habitacion.numero.toString();
        const roomType = roomDto.habitacion.tipoHabitacion;

        for (const date of allDates) {
            
            // 🛑 2.1 INICIALIZACIÓN DEL ESTADO DENTRO DEL BUCLE
            let estado: RoomCellData['estado']; 
            
            // 🛑 2.2 Mapeo de estado base (HABITABLE -> Disponible)
            if (roomDto.habitacion.estado === 'HABITABLE') {
                estado = 'Disponible';
            } else {
                // Mapear cualquier otro estado de Back-End al estado de Front-End
                estado = 'Fuera de servicio'; 
            }

            let reservedBy: string | undefined = undefined;
            let reservedDNI: string | undefined = undefined;
            
            // Convertimos la fecha de comparación a objeto Date
            const targetDate = new Date(date + 'T00:00:00'); // Asegura medianoche local

            // 3. Revisar Ocupaciones (Solo si no está Fuera de Servicio)
            if (estado !== 'Fuera de servicio') {
                for (const ocupacion of roomDto.ocupaciones) {
                    // Nota: Asegúrate que fechaInicio/fechaFin del DTO de ocupación sean válidos
                    const ocupacionStart = new Date(ocupacion.fechaInicio);
                    const ocupacionEnd = new Date(ocupacion.fechaFin);

                    // Comparamos el día destino con el rango de ocupación
                    if (targetDate >= ocupacionStart && targetDate <= ocupacionEnd) {
                        estado = 'Ocupada';
                        break;
                    }
                }
            }
            
            // 4. Revisar Reservas (Solo si NO está Ocupada ni Fuera de Servicio)
            if (estado === 'Disponible') { 
                for (const reserva of roomDto.reservas) {
                    const reservaStart = new Date(reserva.fechaInicio);
                    const reservaEnd = new Date(reserva.fechaFin+ 'T00:00:00');

                    if (targetDate >= reservaStart && targetDate <= reservaEnd) {
                        estado = 'Reservada';
                        // Añadir lógica para reservedBy/reservedDNI si es posible.
                        break;
                    }
                }
            }
            
            // 5. Agregar la celda a la data final
            gridData.push({
                roomId: roomId,
                roomType: roomType,
                date: date,
                estado: estado,
                reservedBy: reservedBy,
                reservedDNI: reservedDNI,
            });
        }
    }

    return gridData;
};