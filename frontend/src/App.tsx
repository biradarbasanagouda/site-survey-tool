import { Routes, Route, Navigate } from 'react-router-dom'
import { useAuthStore } from '@/store/authStore'
import AppLayout from '@/components/layout/AppLayout'
import LoginPage from '@/pages/LoginPage'
import DashboardPage from '@/pages/DashboardPage'
import PropertiesPage from '@/pages/PropertiesPage'
import PropertyDetailPage from '@/pages/PropertyDetailPage'
import BuildingsPage from '@/pages/BuildingsPage'
import FloorsPage from '@/pages/FloorsPage'
import FloorPlanPage from '@/pages/FloorPlanPage'
import SpacesPage from '@/pages/SpacesPage'
import EquipmentPage from '@/pages/EquipmentPage'
import ChecklistsPage from '@/pages/ChecklistsPage'
import RfScansPage from '@/pages/RfScansPage'
import ReportsPage from '@/pages/ReportsPage'

function RequireAuth({ children }: { children: React.ReactNode }) {
  const isAuthenticated = useAuthStore(s => s.isAuthenticated())
  return isAuthenticated ? <>{children}</> : <Navigate to="/login" replace />
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/" element={<RequireAuth><AppLayout /></RequireAuth>}>
        <Route index element={<Navigate to="/dashboard" replace />} />
        <Route path="dashboard"                         element={<DashboardPage />} />
        <Route path="properties"                        element={<PropertiesPage />} />
        <Route path="properties/:id"                    element={<PropertyDetailPage />} />
        <Route path="properties/:propertyId/buildings"  element={<BuildingsPage />} />
        <Route path="buildings/:buildingId/floors"      element={<FloorsPage />} />
        <Route path="floors/:floorId/plan"              element={<FloorPlanPage />} />
        <Route path="floors/:floorId/spaces"            element={<SpacesPage />} />
        <Route path="spaces/:spaceId/equipment"         element={<EquipmentPage />} />
        <Route path="checklists"                        element={<ChecklistsPage />} />
        <Route path="rf-scans"                          element={<RfScansPage />} />
        <Route path="reports"                           element={<ReportsPage />} />
      </Route>
    </Routes>
  )
}
