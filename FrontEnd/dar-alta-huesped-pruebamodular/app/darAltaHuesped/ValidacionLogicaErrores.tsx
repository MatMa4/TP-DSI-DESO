import { FormData } from '../types';

export const validateHuespedForm = (formData: FormData): Record<string, string> => {
  const newErrors: Record<string, string> = {};
  
  const opcionesIVA = ["RESPONSABLE INSCRIPTO", "MONOTRIBUTISTA", "EXENTO", "CONSUMIDOR FINAL"];

  // --- Validaciones Personales ---
  if (!formData.nombre.trim()) newErrors.nombre = 'Campo obligatorio';
  else if (formData.nombre.trim().length < 2) newErrors.nombre = 'Debe tener al menos 2 caracteres';
  else if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.nombre)) newErrors.nombre = 'Solo se permiten letras';

  if (!formData.apellido.trim()) newErrors.apellido = 'Campo obligatorio';
  else if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.apellido)) newErrors.apellido = 'Solo se permiten letras';

  // Validación Fecha
  if (!formData.fechaNacimiento) {
      newErrors.fechaNacimiento = 'Campo obligatorio';
  } else {
      const fechaIngresada = new Date(formData.fechaNacimiento);
      const fechaActual = new Date();
      fechaActual.setHours(0, 0, 0, 0); 
      if (fechaIngresada > fechaActual) {
          newErrors.fechaNacimiento = 'La fecha no puede ser futura';
      }
  }

  if (!formData.telefono.trim()) newErrors.telefono = 'Campo obligatorio';
  else if (!/^\d+$/.test(formData.telefono)) newErrors.telefono = 'Solo se permiten números';

  if (!formData.ocupacion.trim()) newErrors.ocupacion = 'Campo obligatorio';
  else if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.ocupacion)) newErrors.ocupacion = 'Solo se permiten letras';

  if (formData.posicionIVA.trim() && !opcionesIVA.includes(formData.posicionIVA.toUpperCase())) {
    newErrors.posicionIVA = 'Posición IVA inválida, debe ser una de las siguientes: RESPONSABLE INSCRIPTO, MONOTRIBUTISTA, EXENTO, CONSUMIDOR FINAL';
  }

  if (!formData.nacionalidad.trim()) newErrors.nacionalidad = 'Campo obligatorio';
  else if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.nacionalidad)) newErrors.nacionalidad = 'Solo se permiten letras';

  // --- Validación Documento ---
  if (!formData.numeroDocumento.trim()) {
    newErrors.numeroDocumento = 'Campo obligatorio';
  } else {
    switch (formData.tipoDocumento) {
      case 'DNI':
        if (!/^\d+$/.test(formData.numeroDocumento)) newErrors.numeroDocumento = 'El DNI solo debe contener números';
        break;
      case 'LC':
        if (!/^[Ff]\d+$/.test(formData.numeroDocumento)) newErrors.numeroDocumento = 'Debe comenzar con F seguida de números';
        break;
      case 'LE':
        if (!/^[Mm]\d+$/.test(formData.numeroDocumento)) newErrors.numeroDocumento = 'Debe comenzar con M seguida de números';
        break;
      case 'pasaporte':
        if (!/^[A-Za-z0-9]+$/.test(formData.numeroDocumento)) newErrors.numeroDocumento = 'Caracteres inválidos';
        break;
      default:
        if (formData.numeroDocumento.length < 3) newErrors.numeroDocumento = 'Documento inválido';
        break;
    }
  }

  if (formData.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
    newErrors.email = 'Formato de email inválido';
  }

  if (formData.cuit && !/^\d{11}$/.test(formData.cuit)) {
    newErrors.cuit = 'El CUIT debe tener 11 dígitos numéricos';
  }

  // --- Validaciones Dirección ---
  
  if (!formData.direccionHuesped.calle.trim()) newErrors['direccionHuesped.calle'] = 'Campo obligatorio';
  
  // --- VALIDACIÓN INTELIGENTE DE NÚMEROS (Numero, Piso, Codigo) ---
  // Acepta 'number' (valido) o 'string' (se valida contenido)
  
  // 1. Numero
  const valNumero = formData.direccionHuesped.numero;
  if (valNumero === null || valNumero === undefined || (typeof valNumero === 'string' && !valNumero.trim())) {
      newErrors['direccionHuesped.numero'] = 'Campo obligatorio';
  } else if (typeof valNumero === 'string' && !/^\d+$/.test(valNumero)) {
      newErrors['direccionHuesped.numero'] = 'Solo se permiten números';
  }

  // 2. Piso
  const valPiso = formData.direccionHuesped.piso;
  if (valPiso === null || valPiso === undefined || (typeof valPiso === 'string' && !valPiso.trim())) {
      newErrors['direccionHuesped.piso'] = 'Campo obligatorio';
  } else if (typeof valPiso === 'string' && !/^\d+$/.test(valPiso)) {
      newErrors['direccionHuesped.piso'] = 'Solo se permiten números';
  }
  
  // 3. Codigo Postal (Opcional en null, pero si viene string debe ser numérico)
  const valCodigo = formData.direccionHuesped.codigo;
  if (typeof valCodigo === 'string' && valCodigo.trim() !== '' && !/^\d+$/.test(valCodigo)) {
      newErrors['direccionHuesped.codigo'] = 'Solo se permiten números';
  }

  // Resto de campos de texto
  if (!formData.direccionHuesped.localidad.trim()) newErrors['direccionHuesped.localidad'] = 'Campo obligatorio';
  else if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.direccionHuesped.localidad)) newErrors['direccionHuesped.localidad'] = 'Solo se permiten letras';
  
  if (!formData.direccionHuesped.provincia.trim()) newErrors['direccionHuesped.provincia'] = 'Campo obligatorio';
  else if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.direccionHuesped.provincia)) newErrors['direccionHuesped.provincia'] = 'Solo se permiten letras';
  
  if (!formData.direccionHuesped.departamento.trim()) newErrors['direccionHuesped.departamento'] = 'Campo obligatorio';
  else if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.direccionHuesped.departamento)) newErrors['direccionHuesped.departamento'] = 'Solo se permiten letras';
  
  if (!formData.direccionHuesped.pais.trim()) newErrors['direccionHuesped.pais'] = 'Campo obligatorio';
  else if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.direccionHuesped.pais)) newErrors['direccionHuesped.pais'] = 'Solo se permiten letras';

  return newErrors;
};