import httClient from "../http-common";

const RESERVA_API_URL = "/api/reservas"

// Actualizar una reserva por ID
const actualizarReservaPorId = (id, updatedReserva) => {
  return httClient.put(`${RESERVA_API_URL}/updateReservaById/${id}`, updatedReserva);
};

// Obtener información de una reserva por ID
const obtenerInformacionReserva = (id) => {
  return httClient.get(`${RESERVA_API_URL}/getInfoReserva/${id}`);
};

// Eliminar una reserva por ID
const eliminarReservaPorId = (id) => {
  return httClient.delete(`${RESERVA_API_URL}/deleteReservaById/${id}`);
};

export default {
  actualizarReservaPorId,
  obtenerInformacionReserva,
  eliminarReservaPorId
};
