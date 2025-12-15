
import { RoomStatusDTO, RoomCellData } from '../types/indexCU4-5-15';


const formatToYMD = (dateStr: string): string => {
    const date = new Date(dateStr);
    const timezoneOffset = date.getTimezoneOffset() * 60000;
    const correctedDate = new Date(date.getTime() + timezoneOffset);
    return correctedDate.toISOString().split('T')[0];
};

const getDatesInRange = (startStr: string, endStr: string): string[] => {
    const dates = [];
    const currentDate = new Date(startStr + 'T00:00:00'); 
    const endDate = new Date(endStr + 'T00:00:00');
    
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
    const gridData: RoomCellData[] = [];

    // 1. Filtrar las habitaciones por tipo
    const filteredRooms = rawData.filter(roomDto => {
        const normalizedBackendType = roomDto.habitacion.tipoHabitacion.replace(/\s/g, '');
        
        return normalizedBackendType === normalizedTargetType;
    });

    for (const roomDto of filteredRooms) {
        const roomId = roomDto.habitacion.numero.toString();
        const roomType = roomDto.habitacion.tipoHabitacion;

        for (const date of allDates) {
            
            let estado: RoomCellData['estado']; 
            
            if (roomDto.habitacion.estado === 'HABITABLE') {
                estado = 'Disponible';
            } else {
                estado = 'Fuera de servicio'; 
            }

            let reservedBy: string | undefined = undefined;
            let reservedDNI: string | undefined = undefined;
            
            const targetDate = new Date(date + 'T00:00:00'); 

            if (estado !== 'Fuera de servicio') {
                for (const ocupacion of roomDto.ocupaciones) {
                    const ocupacionStart = new Date(ocupacion.fechaInicio);
                    const ocupacionEnd = new Date(ocupacion.fechaFin);

                    if (targetDate >= ocupacionStart && targetDate <= ocupacionEnd) {
                        estado = 'Ocupada';
                        break;
                    }
                }
            }
            
            if (estado === 'Disponible') { 
                for (const reserva of roomDto.reservas) {
                    const reservaStart = new Date(reserva.fechaInicio);
                    const reservaEnd = new Date(reserva.fechaFin + 'T00:00:00');

                    if (targetDate >= reservaStart && targetDate <= reservaEnd) {
                        estado = 'Reservada';
                        break;
                    }
                }
            }
            
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