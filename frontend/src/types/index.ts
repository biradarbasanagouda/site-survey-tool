// ─── Auth ───────────────────────────────────────────────────────────────────
export interface User {
  id: number
  email: string
  fullName: string
  createdAt: string
}

export interface AuthResponse {
  accessToken: string
  refreshToken: string
  tokenType: string
  expiresIn: number
  user: User
}

// ─── Organization ───────────────────────────────────────────────────────────
export interface Organization {
  id: number
  name: string
  createdAt: string
}

// ─── Property ───────────────────────────────────────────────────────────────
export interface Property {
  id: number
  organizationId: number
  name: string
  addressLine1?: string
  addressLine2?: string
  city?: string
  state?: string
  postalCode?: string
  country?: string
  boundaryType?: string
  boundaryWkt?: string
  centroidLat?: number
  centroidLon?: number
  createdAt: string
  updatedAt: string
}

// ─── Building ────────────────────────────────────────────────────────────────
export interface Building {
  id: number
  propertyId: number
  name: string
  code?: string
  footprintType?: string
  footprintWkt?: string
  floorsCount: number
  createdAt: string
  updatedAt: string
}

// ─── Floor ───────────────────────────────────────────────────────────────────
export interface Floor {
  id: number
  buildingId: number
  levelLabel: string
  elevationM: number
  planFileId?: number
  scaleRatio?: string
  createdAt: string
  updatedAt: string
}

// ─── Space ───────────────────────────────────────────────────────────────────
export interface Space {
  id: number
  floorId: number
  name: string
  type: SpaceType
  geometryType?: string
  geometryWkt?: string
  areaSqM?: number
  elevationM?: number
  notes?: string
  createdAt: string
  updatedAt: string
}

export type SpaceType =
  | 'APARTMENT' | 'OFFICE' | 'SERVER_ROOM' | 'UTILITY'
  | 'CORRIDOR' | 'LOBBY' | 'ROOFTOP' | 'BASEMENT' | 'PARKING' | 'OTHER'

// ─── Equipment ───────────────────────────────────────────────────────────────
export interface Equipment {
  id: number
  spaceId: number
  type: string
  model?: string
  vendor?: string
  powerWatts?: number
  serialNumber?: string
  createdAt: string
}

// ─── Checklist ───────────────────────────────────────────────────────────────
export interface ChecklistTemplate {
  id: number
  organizationId: number
  name: string
  scope: string
  version: number
  schemaJson: string
  isActive: boolean
}

export interface ChecklistResponse {
  id: number
  templateId: number
  targetType: string
  targetId: number
  answersJson?: string
  submittedAt?: string
  createdAt: string
}

// ─── RF Scan ─────────────────────────────────────────────────────────────────
export interface RfScan {
  id: number
  propertyId: number
  floorId?: number
  tool: string
  parsedJson?: string
  createdAt: string
}

// ─── Report ──────────────────────────────────────────────────────────────────
export interface Report {
  id: number
  propertyId: number
  status: 'PENDING' | 'GENERATING' | 'DONE' | 'FAILED'
  createdAt: string
  updatedAt: string
}

// ─── File ────────────────────────────────────────────────────────────────────
export interface FileRecord {
  id: number
  filename: string
  contentType: string
  sizeBytes: number
  ownerType: string
  ownerId: number
  createdAt: string
}

// ─── Dashboard ───────────────────────────────────────────────────────────────
export interface DashboardStats {
  totalProperties: number
  totalBuildings: number
  totalFloors: number
  totalSpaces: number
  totalEquipment: number
  totalChecklistResponses: number
  pendingReports: number
}

// ─── Pagination ───────────────────────────────────────────────────────────────
export interface PageResponse<T> {
  content: T[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  last: boolean
}
