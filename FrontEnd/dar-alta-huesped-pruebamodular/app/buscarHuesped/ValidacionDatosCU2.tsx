import { FormData } from '../types';

export function validateBuscarForm(formData: FormData): Record<string, string> {
  const newErrors: Record<string, string> = {};

  // Nombre: obligatorio, mínimo 2 caracteres, solo letras y espacios
  if (formData.nombre.trim().length < 2) newErrors.nombre = 'Debe tener al menos 2 caracteres';
  else if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.nombre)) newErrors.nombre = 'Solo se permiten letras';

// Apellido: solo letras y espacios
if (formData.apellido && !/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.apellido)) {
    newErrors.apellido = 'Solo se permiten letras';
}

// Número y tipo de documento: mismas reglas que ValidacionLogicaErrores
if (formData.numeroDocumento.trim()) {
switch (formData.tipoDocumento) {
    case 'DNI':
    if (!/^\d+$/.test(formData.numeroDocumento)) {
        newErrors.numeroDocumento = 'El DNI solo debe contener números';
    }
    break;
    case 'LC':
    if (!/^[Ff]\d+$/.test(formData.numeroDocumento)) {
        newErrors.numeroDocumento = 'Debe comenzar con F seguida de números';
    }
    break;
    case 'LE':
    if (!/^[Mm]\d+$/.test(formData.numeroDocumento)) {
        newErrors.numeroDocumento = 'Debe comenzar con M seguida de números';
    }
    break;
    case 'pasaporte':
    if (!/^[A-Za-z0-9]+$/.test(formData.numeroDocumento)) {
        newErrors.numeroDocumento = 'El pasaporte solo puede contener letras y números';
    }
    break;
    default:
    if (formData.numeroDocumento.length < 3) {
        newErrors.numeroDocumento = 'Documento inválido';
    }
    break;
  }
}

  return newErrors;
}
