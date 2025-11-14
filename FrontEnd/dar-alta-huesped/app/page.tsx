'use client';
import '../styles.css';
import React, { useState } from 'react';
export default function Home() {
  const [formData, setFormData] = useState({
    numeroDocumento: '',
    tipoDocumento: 'DNI',
    apellido: '',
    nombre: '',
    fechaNacimiento: '',
    telefono: '',
    email: '',
    ocupacion: '',
    nacionalidad: '',
    cuit: '',
    posicionIVA: '',
    alojado: false,
    direccionHuesped: {
      calle: '',
      numero: '',
      departamento: '',
      piso: '',
      codigo: '',
      localidad: '',
      provincia: '',
      pais: '',
    },
  });
const INITIAL_FORM = {
  numeroDocumento: '',
  tipoDocumento: 'DNI',
  apellido: '',
  nombre: '',
  fechaNacimiento: '',
  telefono: '',
  email: '',
  ocupacion: '',
  nacionalidad: '',
  cuit: '',
  posicionIVA: '',
  alojado: false,
  direccionHuesped: {
    calle: '',
    numero: '',
    departamento: '',
    piso: '',
    codigo: '',
    localidad: '',
    provincia: '',
    pais: '',
  },
};
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [showModal, setShowModal] = useState(false);
  const [modalMessage, setModalMessage] = useState('');
  const [pendingFinalData, setPendingFinalData] = useState<any | null>(null);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [highlightDocumento, setHighlightDocumento] = useState(false);
  const [showCompletionScreen, setShowCompletionScreen] = useState(false);
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target;
    const checked = (e.target as HTMLInputElement).checked;

    // Si el input pertenece a la dirección (nombre con "direccionHuesped.")
    if (name.startsWith('direccionHuesped.')) {
      const field = name.split('.')[1];
      setFormData((prev) => ({
        ...prev,
        direccionHuesped: {
          ...prev.direccionHuesped,
          [field]: value,
        },
      }));
      setErrors((prev) => ({ ...prev, [name]: '' })); // borra el error si el usuario escribe
    } else {
      setFormData({
        ...formData,
        [name]: type === 'checkbox' ? checked : value,
      });
      setErrors((prev) => ({ ...prev, [name]: '' }));
      // Si el usuario empieza a escribir en el documento, quitar el highlight
      if (name === 'numeroDocumento' || name === 'tipoDocumento') {
        setHighlightDocumento(false);
      }
    }
  };
    const handleCancelClick = (e: React.MouseEvent) => {
    e.preventDefault();
    setShowCancelModal(true);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const newErrors: any = {};
    const opcionesIVA = ["RESPONSABLE INSCRIPTO", "MONOTRIBUTISTA", "EXENTO", "CONSUMIDOR FINAL"];
    if (!formData.nombre.trim()) newErrors.nombre = 'Campo obligatorio';
    else if (formData.nombre.trim().length < 2) newErrors.nombre = 'Debe tener al menos 2 caracteres';
    else if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.nombre)) newErrors.nombre = 'Solo se permiten letras';
    if (!formData.apellido.trim()) newErrors.apellido = 'Campo obligatorio';
    else if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.apellido)) newErrors.apellido = 'Solo se permiten letras';
    if (!formData.fechaNacimiento) newErrors.fechaNacimiento = 'Campo obligatorio';
    if (!formData.telefono.trim()) newErrors.telefono = 'Campo obligatorio';
    else if (!/^\d+$/.test(formData.telefono)) newErrors.telefono = 'Solo se permiten números';
    if (!formData.ocupacion.trim()) newErrors.ocupacion = 'Campo obligatorio';
    else if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.ocupacion)) newErrors.ocupacion = 'Solo se permiten letras';
    //if (!formData.posicionIVA.trim()) newErrors.posicionIVA = 'Campo obligatorio';
    if (!opcionesIVA.includes(formData.posicionIVA.toUpperCase())) {
    newErrors.posicionIVA = 'Debe ser una de las siguientes opciones: RESPONSABLE INSCRIPTO, MONOTRIBUTISTA, EXENTO o CONSUMIDOR FINAL';
    } 
    if (!formData.nacionalidad.trim()) newErrors.nacionalidad = 'Campo obligatorio';
    else if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.nacionalidad)) newErrors.nacionalidad = 'Solo se permiten letras';
    if (!formData.numeroDocumento.trim()) {
    newErrors.numeroDocumento = 'Campo obligatorio';
    } else {
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
    if (formData.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = 'Formato de email inválido';
    }
    if (formData.cuit && !/^\d{11}$/.test(formData.cuit)) {
      newErrors.cuit = 'El CUIT debe tener 11 dígitos numéricos';
    }
    if (!formData.direccionHuesped.calle.trim()) newErrors['direccionHuesped.calle'] = 'Campo obligatorio';
    if (!formData.direccionHuesped.numero.trim()) newErrors['direccionHuesped.numero'] = 'Campo obligatorio';
    else if (!/^\d+$/.test(formData.direccionHuesped.numero)) newErrors['direccionHuesped.numero'] = 'Solo se permiten números';
    if (!formData.direccionHuesped.piso.trim()) newErrors['direccionHuesped.piso'] = 'Campo obligatorio';
    else if (!/^\d+$/.test(formData.direccionHuesped.piso)) newErrors['direccionHuesped.piso'] = 'Solo se permiten números';
    if (!formData.direccionHuesped.localidad.trim()) newErrors['direccionHuesped.localidad'] = 'Campo obligatorio';
    else if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.direccionHuesped.localidad)) newErrors['direccionHuesped.localidad'] = 'Solo se permiten letras';
    if (!formData.direccionHuesped.provincia.trim()) newErrors['direccionHuesped.provincia'] = 'Campo obligatorio';
    else if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.direccionHuesped.provincia)) newErrors['direccionHuesped.provincia'] = 'Solo se permiten letras';
    if (!formData.direccionHuesped.departamento.trim()) newErrors['direccionHuesped.departamento'] = 'Campo obligatorio';
    else if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.direccionHuesped.departamento)) newErrors['direccionHuesped.departamento'] = 'Solo se permiten letras';
    if (!formData.direccionHuesped.pais.trim()) newErrors['direccionHuesped.pais'] = 'Campo obligatorio';
    else if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$/.test(formData.direccionHuesped.pais)) newErrors['direccionHuesped.pais'] = 'Solo se permiten letras';
    if (formData.direccionHuesped.codigo && !/^\d+$/.test(formData.direccionHuesped.codigo)) newErrors['direccionHuesped.codigo'] = 'Solo se permiten números';
    

    setErrors(newErrors);

    let finalData = formData;
    if (Object.keys(newErrors).length === 0) {
      const transformedData = {
        ...formData,
        numeroDocumento: formData.numeroDocumento ? formData.numeroDocumento.toUpperCase() : formData.numeroDocumento,
        tipoDocumento: formData.tipoDocumento ? formData.tipoDocumento.toUpperCase() : formData.tipoDocumento,
        apellido: formData.apellido ? formData.apellido.toUpperCase() : formData.apellido,
        nombre: formData.nombre ? formData.nombre.toUpperCase() : formData.nombre,
        telefono: formData.telefono? formData.telefono.toUpperCase() : formData.telefono,
        email: formData.email? formData.email.toUpperCase() : formData.email,
        ocupacion: formData.ocupacion? formData.ocupacion.toUpperCase() : formData.ocupacion,
        nacionalidad: formData.nacionalidad? formData.nacionalidad.toUpperCase() : formData.nacionalidad,
        cuit: formData.cuit,
       // posicionIVA: formData.posicionIVA? formData.posicionIVA.toUpperCase() : formData.posicionIVA,
        posicionIVA: formData.posicionIVA.trim()? formData.posicionIVA.toUpperCase() : "CONSUMIDOR FINAL",
        fechaNacimiento: formData.fechaNacimiento,
        alojado: formData.alojado,
        direccionHuesped: {
          calle: formData.direccionHuesped.calle? formData.direccionHuesped.calle.toUpperCase() : formData.direccionHuesped.calle,
          numero: formData.direccionHuesped.numero,
          piso: formData.direccionHuesped.piso,
          codigo: formData.direccionHuesped.codigo,
          departamento: formData.direccionHuesped.departamento? formData.direccionHuesped.departamento.toUpperCase() : formData.direccionHuesped.departamento,
          localidad: formData.direccionHuesped.localidad? formData.direccionHuesped.localidad.toUpperCase() : formData.direccionHuesped.localidad,
          provincia: formData.direccionHuesped.provincia? formData.direccionHuesped.provincia.toUpperCase() : formData.direccionHuesped.provincia,
          pais: formData.direccionHuesped.pais? formData.direccionHuesped.pais.toUpperCase() : formData.direccionHuesped.pais
        }
      };
            // actualizar el formulario para que el usuario vea el apellido en mayúsculas
      setFormData(transformedData);
      finalData = transformedData;

      try {
        const res = await fetch('http://localhost:8080/huespedes', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(finalData),
        });

        if (res.ok) {
          const saved = await res.json().catch(() => null);
          console.log('✅ Huésped guardado:', saved ?? 'No body');
          // Mostrar modal de éxito con opción de cargar otro
          setSuccessMessage(
            `El huésped ${finalData.nombre ?? ''} ${finalData.apellido ?? ''} ha sido satisfactoriamente cargado al sistema\n¿Desea cargar otro?`
          );
          setShowSuccessModal(true);
          // opcional: limpiar pendingFinalData si estaba
          setPendingFinalData(null);
        } else if (res.status === 409) {
          setModalMessage('El tipo y número de documento ya existen en el sistema');
          setPendingFinalData(finalData); // <-- guardar datos que queremos reenviar si el usuario acepta
          setShowModal(true);
        }
      } catch (err) {
        console.error('❌ Error de conexión al backend:', err);
      }
    }    
  };
  const resetForm = () => {
    setFormData(INITIAL_FORM);
    setErrors({});
  };
  return (
    <main>
       <div className="tittle_box">
          <h1 className="main_title">Completar los datos</h1>
        </div>

      <form className="main_box" onSubmit={handleSubmit} noValidate>
        <div className="container">
          <div className="box1">
            <div className="box1_simplebox">
              <label htmlFor="Nombre">Nombre <span style={{color: 'red'}}>*</span></label>
              <input 
                className="input_box" 
                type="text" 
                name="nombre"
                value ={formData.nombre}
                onChange={handleChange}
                
              />
              {errors.nombre && <p className="error">{errors.nombre}</p>}
            </div>

            <div className="box1_simplebox">
              <label htmlFor="CUIT">CUIT</label>
              <input 
                className="input_box" 
                type="text" 
                name="cuit"
                value={formData.cuit}
                onChange={handleChange}
              />
              {errors.cuit && <p className="error">{errors.cuit}</p>}
            </div>

            <div className="box1_simplebox">
              <label htmlFor="Fecha">Fecha de Nacimiento *</label>
              <input 
                className="input_box" 
                type="date" 
                name="fechaNacimiento"
                value={formData.fechaNacimiento}
                onChange={handleChange}
              />
              {errors.fechaNacimiento && <p className="error">{errors.fechaNacimiento}</p>}
            </div>
          </div>

          <div className="box1">
            <div className="box1_simplebox">
              <label htmlFor="Apellido">Apellido <span style={{color: 'red'}}>*</span></label>
              <input 
                className="input_box" 
                type="text" 
                name="apellido"
                value={formData.apellido}
                onChange={handleChange}
                 
              />
              {errors.apellido && <p className="error">{errors.apellido}</p>}
            </div>

            <div className="box1_simplebox">
              <label>Teléfono <span style={{color: 'red'}}>*</span></label>
              <input 
                className="input_box" 
                type="tel" 
                name="telefono" 
                value={formData.telefono}
                onChange={handleChange}
              />
               {errors.telefono && <p className="error">{errors.telefono}</p>}
            </div>

            <div className="box1_simplebox">
              <label htmlFor="posicionIVA">posicionIVA </label>
              <input 
                className="input_box" 
                type="text" 
                name="posicionIVA"
                value={formData.posicionIVA}
                onChange={handleChange}
              />
              {errors.posicionIVA && <p className="error">{errors.posicionIVA}</p>}
            </div>
          </div>

          <div className="box1 boxDocumento">
            <label>Documento <span style={{color: 'red'}}>*</span></label>
            <div className="documento_inputs">
              <select 
              className="input_box" 
              name="tipoDocumento" 
              value={formData.tipoDocumento}
              onChange={handleChange}
              style={highlightDocumento ? { borderColor: 'red', borderWidth: '3px' } : {}}
              >
                <option value="dni">DNI</option>
                <option value="LE">LE</option>
                <option value="LC">LC</option>
                <option value="pasaporte">Pasaporte</option>
                <option value="Otro">Otro</option>
              </select>
              <input 
                className="input_box_documento" 
                type="text" 
                name="numeroDocumento" 
                value={formData.numeroDocumento}
                onChange={handleChange}
                style={highlightDocumento ? { borderColor: 'red', borderWidth: '3px' } : {}}
              />
            </div>
            {errors.numeroDocumento && <p className="error">{errors.numeroDocumento}</p>}
          </div>
        </div>

         <div className='container'>
            <div className="box1">
                <div className="box1_simplebox">
                    <label>Calle <span style={{color: 'red'}}>*</span></label>
                    <input 
                      className="input_box" 
                      type="text" 
                      name="direccionHuesped.calle"
                      value={formData.direccionHuesped.calle}
                      onChange={handleChange}
                        
                    />
                    {errors['direccionHuesped.calle'] && <p className="error">{errors['direccionHuesped.calle']}</p>}
                </div>

                <div className="box1_simplebox">
                    <label>Codigo Postal</label>
                    <input 
                      className="input_box" 
                      type="text" 
                      name="direccionHuesped.codigo"
                      value={formData.direccionHuesped.codigo}
                      onChange={handleChange}
                    />
                    {errors['direccionHuesped.codigo'] && <p className="error">{errors['direccionHuesped.codigo']}</p>}
                </div>
            </div>
            <div className="box1">
                    
                    <div className="box1_simplebox">
                        <label>Número <span style={{color: 'red'}}>*</span></label>
                        <input  
                          className="input_box" 
                          type="text" 
                          name="direccionHuesped.numero"
                          value={formData.direccionHuesped.numero}
                          onChange={handleChange}
                        />
                        {errors['direccionHuesped.numero'] && <p className="error">{errors['direccionHuesped.numero']}</p>}
                    </div>
                    <div className="box1_simplebox">
                        <label>Localidad <span style={{color: 'red'}}>*</span></label>
                        <input  
                          className="input_box" 
                          type="text" 
                          name="direccionHuesped.localidad"
                          value={formData.direccionHuesped.localidad}
                          onChange={handleChange} 
                        />
                        {errors['direccionHuesped.localidad'] && <p className="error">{errors['direccionHuesped.localidad']}</p>}
                    </div>
            </div>
            <div className="box1">
                <div className="box1_simplebox">
                    <label>Piso <span style={{color: 'red'}}>*</span></label>
                    <input 
                      className="input_box" 
                      type="text" 
                      name="direccionHuesped.piso"
                      value={formData.direccionHuesped.piso}
                      onChange={handleChange}

                    />
                    {errors['direccionHuesped.piso'] && <p className="error">{errors['direccionHuesped.piso']}</p>}
                </div>
                <div className="box1_simplebox">
                    <label>Provincia <span style={{color: 'red'}}>*</span></label>
                    <input 
                      className="input_box" 
                      type="text" 
                      name="direccionHuesped.provincia" 
                      value={formData.direccionHuesped.provincia}
                      onChange={handleChange}
                    />
                    {errors['direccionHuesped.provincia'] && <p className="error">{errors['direccionHuesped.provincia']}</p>}
                </div>

                
            </div>
            <div className="box1">
                <div className="box1_simplebox">
                    <label>Departamento <span style={{color: 'red'}}>*</span></label>
                    <input 
                    className="input_box" 
                    type="text" 
                    name="direccionHuesped.departamento"
                    value={formData.direccionHuesped.departamento}
                    onChange={handleChange}
                    />
                    {errors['direccionHuesped.departamento'] && <p className="error">{errors['direccionHuesped.departamento']}</p>}
                </div>
                <div className="box1_simplebox">
                    <label>País <span style={{color: 'red'}}>*</span></label>
                    <input 
                      className="input_box" 
                      type="text" 
                      name="direccionHuesped.pais"
                      value={formData.direccionHuesped.pais}
                      onChange={handleChange} 
                    />
                    {errors['direccionHuesped.pais'] && <p className="error">{errors['direccionHuesped.pais']}</p>}
                </div>
            </div>
        </div>
        <div className="container">
            <div className="box1">
                <label>Email</label>
                <input 
                  className="input_box" 
                  type="email" 
                  name="email" 
                  value={formData.email}
                  onChange={handleChange}
                />
                {errors.email && <p className="error">{errors.email}</p>}    
            </div>

            <div className="box1">
                <label>Ocupación <span style={{color: 'red'}}>*</span></label>
                <input 
                  className="input_box" 
                  type="text" 
                  name="ocupacion" 
                  value={formData.ocupacion}
                  onChange={handleChange}
                />
                {errors.ocupacion && <p className="error">{errors.ocupacion}</p>}
            </div>

            <div className="box1">
                <label>Nacionalidad *</label>
                <input 
                  className="input_box" 
                  type="text" 
                  name="nacionalidad" 
                  value={formData.nacionalidad}
                  onChange={handleChange}
                />
                {errors.nacionalidad && <p className="error">{errors.nacionalidad}</p>}
            </div>         
        </div>   

        <div className="container">
            <div className="box1">
                 <button className="button2" type="button" onClick={handleCancelClick}>CANCELAR</button>
            </div>
            <div className="box1">
                <p><small><span style={{color: 'red'}}>*</span> Campos obligatorios</small></p>
            </div>
            <div className="box1">
                <button className="button2" type="submit">SIGUIENTE</button>    
            </div>
        </div>
      </form>
      {showModal && (
        <div className="modal-overlay">
          <div className="modal">
            <div className="modal-icon">⚠️</div>
            <h2>DNI Repetido</h2>
            <p>{modalMessage}</p>
            <div className="modal-buttons">
              <button className="btn-cancel" onClick={() => {
                setHighlightDocumento(true);
                setShowModal(false)
                }}>
                CORREGIR
              </button>
              <button
                className="btn-accept yellow"
                onClick={async () => {
                  setShowModal(false);
                  setHighlightDocumento(false);
                  if (!pendingFinalData) return;
                  try {
                    const res = await fetch('http://localhost:8080/huespedes?forzar=true', {
                      method: 'POST',
                      headers: { 'Content-Type': 'application/json' },
                      body: JSON.stringify(pendingFinalData),
                    });
                    if (res.ok) {
                      console.log('Aceptar igualmente: registrado forzado');
                      console.log(pendingFinalData);
                      setSuccessMessage(
                      `El huésped ${pendingFinalData.nombre ?? ''} ${pendingFinalData.apellido ?? ''} ha sido satisfactoriamente cargado al sistema\n¿Desea cargar otro?`
                    );
                    setShowSuccessModal(true);
                    } else {
                      console.error('Error al forzar registro:', res.status);
                    }
                  } catch (e) {
                    console.error('Error de conexión al forzar:', e);
                  } finally {
                    setPendingFinalData(null);
                  }
                }}
              >
                ACEPTAR IGUALMENTE
              </button>
            </div>
          </div>
        </div>
      )}
      {showCancelModal && (
        <div className="modal-overlay">
          <div className="modal">
            <div className="modal-icon">⚠️</div>
            <h2>CANCELAR</h2>
            <p>¿Desea cancelar esta carga del huésped?</p>
            <div className="modal-buttons">
              <button className="btn-cancel" onClick={() => setShowCancelModal(false)}>
                NO
              </button>
              <button className="btn-accept yellow" onClick={() => {
                resetForm();
                setShowCancelModal(false);
                setShowCompletionScreen(true);
              }}>
                SI
              </button>
            </div>
          </div>
        </div>
      )}
      
      {showSuccessModal && (
        <div className="modal-overlay">
          <div className="modal">
            <div className="modal-icon">✅</div>
            <h2>Éxito al cargar</h2>
            <p>{successMessage}</p>
            <div className="modal-buttons" style={{ justifyContent: 'center', gap: '20px' }}>
              <button
                className="btn-cancel"
                onClick={() => {
                  // NO -> cerrar modal (puede navegar o quedarse)
                  setShowCompletionScreen(true);
                  setShowSuccessModal(false);
                }}
              >
                NO
              </button>
              <button
                className="btn-accept"
                onClick={() => {
                  // SI -> limpiar todos los campos para volver a ingresar
                  resetForm();
                  setShowSuccessModal(false);
                }}
              >
                SI
              </button>
            </div>
          </div>
        </div>
      )}
      {showCompletionScreen && (
        <div className="completion-screen">
          Caso de Uso Terminado
        </div>
      )}
    </main>
  );
};
