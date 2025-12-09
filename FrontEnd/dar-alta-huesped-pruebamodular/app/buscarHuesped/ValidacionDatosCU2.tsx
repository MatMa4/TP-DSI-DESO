import { FormData } from '../types';

export function validateBuscarForm(formData: FormData): Record<string, string> {
  const newErrors: Record<string, string> = {};

  // VALIDACIÓN NOMBRE
  // Si hay texto,se verificamos que sean solo letras. Si está vacío, no pasa nada.
  if (formData.nombre.trim()) {
      if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.nombre)) {
          newErrors.nombre = 'Solo se permiten letras';
      }
  }

  // VALIDACIÓN APELLIDO
  if (formData.apellido.trim()) {
      if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.apellido)) {
          newErrors.apellido = 'Solo se permiten letras';
      }
  }

  // VALIDACIÓN DOCUMENTO
  // Solo validamos formato si el campo tiene datos
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
            // Validación genérica mínima si es "Otro"
            if (formData.numeroDocumento.length < 3) {
                newErrors.numeroDocumento = 'Documento inválido';
            }
            break;
    }
  }

  return newErrors;
}