// Asumo que estas interfaces están definidas en tu archivo de tipos
import { RoomStatusDTO, RoomCellData } from '../types/indexCU4-5-15';


const formatToYMD = (dateStr: string): string => {
    // Usamos el constructor de Date para manejar ISO y extraemos solo la fecha.
    // Esto es CRÍTICO para asegurar que la comparación sea justa (solo el día).
    const date = new Date(dateStr);
    
    // Si la cadena ya es YYYY-MM-DD, a veces new Date() la interpreta como UTC 00:00, 
    // lo que puede cambiar el día en la zona horaria local. Usamos getTimezoneOffset para corregir.
    const timezoneOffset = date.getTimezoneOffset() * 60000;
    const correctedDate = new Date(date.getTime() + timezoneOffset);
    
    return correctedDate.toISOString().split('T')[0];
};


const getDatesInRange = (startStr: string, endStr: string): string[] => {
    const dates = [];
    const currentDate = new Date(startStr);
    const endDate = new Date(endStr);
    
    // Corrección para evitar desfase de zona horaria al iterar
    currentDate.setDate(currentDate.getDate()); 

    while (currentDate <= endDate) {
        // Usamos el formateo a YYYY-MM-DD para evitar problemas con la hora
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
    
    const allDates = getDatesInRange(fechaInicio, fechaFin);
    const gridData: RoomCellData[] = [];

    // 1. Filtrar las habitaciones por tipo
    const filteredRooms = rawData.filter(roomDto => 
        roomDto.habitacion.tipoHabitacion === targetRoomType
    );

    // 2. Iterar sobre cada habitación y cada día dentro del rango
    for (const roomDto of filteredRooms) {
        const roomId = roomDto.habitacion.numero.toString();
        const roomType = roomDto.habitacion.tipoHabitacion;

        for (const date of allDates) {
            let estado: RoomCellData['estado'] = 'Disponible';
            let reservedBy: string | undefined = undefined;
            let reservedDNI: string | undefined = undefined;
            
            // Convertimos la fecha de comparación a objeto Date
            const targetDate = new Date(date);

            // 3. Revisar Ocupaciones
            for (const ocupacion of roomDto.ocupaciones) {
                const ocupacionStart = new Date(ocupacion.fechaInicio);
                const ocupacionEnd = new Date(ocupacion.fechaFin);

                // Comparamos el día destino con el rango de ocupación
                if (targetDate >= ocupacionStart && targetDate <= ocupacionEnd) {
                    estado = 'Ocupada';
                    // Nota: Si el Back-End incluye el huésped en ocupaciones, lo extraemos aquí.
                    // Si no, solo mostramos 'Ocupada'.
                    break;
                }
            }
            
            // 4. Revisar Reservas (Solo si no está Ocupada)
            if (estado === 'Disponible') {
                for (const reserva of roomDto.reservas) {
                    // ASUMIMOS que el Back-End DTO de Reserva tiene fechaInicio y fechaFin
                    const reservaStart = new Date(reserva.fechaInicio);
                    const reservaEnd = new Date(reserva.fechaFin);

                    if (targetDate >= reservaStart && targetDate <= reservaEnd) {
                        estado = 'Reservada';
                        // ASUMIMOS que el Back-End DTO de Reserva tiene los datos del Huésped
                        // Ej: reservedBy = reserva.huespedNombre;
                        // Ej: reservedDNI = reserva.huespedDNI;
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
                // reservationId, reservedBy, reservedDNI se añaden si se extraen
            });
        }
    }

    return gridData;
};