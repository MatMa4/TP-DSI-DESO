'use client';
import { useState } from 'react';
import '../styles.css';

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
    posicionIVA: 'Consumidor Final',
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

   const [errors, setErrors] = useState<Record<string, string>>({});

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value, type, checked } = e.target;

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
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const newErrors: any = {};
    if (!formData.nombre.trim()) newErrors.nombre = 'Campo obligatorio';
    else if (formData.nombre.trim().length < 2) newErrors.nombre = 'Debe tener al menos 2 caracteres';

    if (!formData.apellido.trim()) newErrors.apellido = 'Campo obligatorio';
    if (!formData.fechaNacimiento) newErrors.fechaNacimiento = 'Campo obligatorio';
    if (!formData.telefono.trim()) newErrors.telefono = 'Campo obligatorio';
    if (!formData.ocupacion.trim()) newErrors.ocupacion = 'Campo obligatorio';
    if (!formData.posicionIVA.trim()) newErrors.posicionIVA = 'Campo obligatorio';
    if (!formData.nacionalidad.trim()) newErrors.nacionalidad = 'Campo obligatorio';
    if (!formData.numeroDocumento.trim()) newErrors.numeroDocumento = 'Campo obligatorio';
    if (formData.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = 'Formato de email inválido';
    }
    if (formData.cuit && !/^\d{11}$/.test(formData.cuit)) {
      newErrors.cuit = 'El CUIT debe tener 11 dígitos numéricos';
    }
    for (const field in formData.direccionHuesped) {
    const value = formData.direccionHuesped[field as keyof typeof formData.direccionHuesped];
      if (typeof value !== 'string' || !value.trim()) {
        newErrors[`direccionHuesped.${field}`] = 'Campo obligatorio';
      }
    }

    setErrors(newErrors);
    if (Object.keys(newErrors).length === 0) {
      console.log('✅ Formulario válido:', formData);
    }

    console.log(formData); // Por ahora solo muestra los datos en consola
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
              <label htmlFor="posicionIVA">posicionIVA <span style={{color: 'red'}}>*</span></label>
              <input 
                className="input_box" 
                type="text" 
                name="posicionIVA"
                value={formData.posicionIVA}
                onChange={handleChange}
              />
              {errors.posicionIVA && <p className="error">{errors.posicionIVA}</p>}
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
          </div>

          <div className="box1 boxDocumento">
            <label>Documento <span style={{color: 'red'}}>*</span></label>
            <div className="documento_inputs">
              <select 
              className="input_box" 
              name="tipoDocumento" 
              value={formData.tipoDocumento}
              onChange={handleChange}
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
                <button className="button2" type="reset">CANCELAR</button>
            </div>
            <div className="box1">
                <p><small><span style={{color: 'red'}}>*</span> Campos obligatorios</small></p>
            </div>
            <div className="box1">
                <button className="button2" type="submit">SIGUIENTE</button>    
            </div>
        </div> 
      </form>
    </main>
  );
};