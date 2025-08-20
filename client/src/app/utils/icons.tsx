import { Ionicons } from "@expo/vector-icons"

// Icon mapping from lucide-react-native to @expo/vector-icons
export const Icons = {
  // Navigation
  ArrowLeft: (props: any) => <Ionicons name="arrow-back" {...props} />,
  ArrowRight: (props: any) => <Ionicons name="arrow-forward" {...props} />,
  ArrowUpRight: (props: any) => <Ionicons name="arrow-up" {...props} />,
  ArrowDownLeft: (props: any) => <Ionicons name="arrow-down" {...props} />,
  ChevronRight: (props: any) => <Ionicons name="chevron-forward" {...props} />,
  ChevronDown: (props: any) => <Ionicons name="chevron-down" {...props} />,

  // Actions
  Plus: (props: any) => <Ionicons name="add" {...props} />,
  PlusCircle: (props: any) => <Ionicons name="add-circle" {...props} />,
  X: (props: any) => <Ionicons name="close" {...props} />,
  Search: (props: any) => <Ionicons name="search" {...props} />,
  Mic: (props: any) => <Ionicons name="mic" {...props} />,
  Eye: (props: any) => <Ionicons name="eye" {...props} />,
  EyeOff: (props: any) => <Ionicons name="eye-off" {...props} />,
  Filter: (props: any) => <Ionicons name="filter" {...props} />,
  Upload: (props: any) => <Ionicons name="cloud-upload" {...props} />,
  Pencil: (props: any) => <Ionicons name="create" {...props} />,
  Link2: (props: any) => <Ionicons name="link" {...props} />,
  Trash2: (props: any) => <Ionicons name="trash" {...props} />,
  Refresh: (props: any) => <Ionicons name="refresh" {...props} />,

  // UI Elements
  User: (props: any) => <Ionicons name="person" {...props} />,
  Settings: (props: any) => <Ionicons name="settings" {...props} />,
  Bell: (props: any) => <Ionicons name="notifications" {...props} />,
  Heart: (props: any) => <Ionicons name="heart" {...props} />,
  Star: (props: any) => <Ionicons name="star" {...props} />,
  Clock: (props: any) => <Ionicons name="time" {...props} />,
  Moon: (props: any) => <Ionicons name="moon" {...props} />,
  Sun: (props: any) => <Ionicons name="sunny" {...props} />,
  FileText: (props: any) => <Ionicons name="document-text" {...props} />,
  HelpCircle: (props: any) => <Ionicons name="help-circle" {...props} />,
  LogOut: (props: any) => <Ionicons name="log-out" {...props} />,
  Shield: (props: any) => <Ionicons name="shield" {...props} />,
  Lock: (props: any) => <Ionicons name="lock-closed" {...props} />,
  Fingerprint: (props: any) => <Ionicons name="finger-print" {...props} />,
  AlertTriangle: (props: any) => <Ionicons name="warning" {...props} />,

  // Payment/Finance
  CreditCard: (props: any) => <Ionicons name="card" {...props} />,
  Wallet: (props: any) => <Ionicons name="wallet" {...props} />,
  Send: (props: any) => <Ionicons name="send" {...props} />,
  Receipt: (props: any) => <Ionicons name="receipt" {...props} />,
  ReceiptCent: (props: any) => <Ionicons name="receipt" {...props} />,
  Banknote: (props: any) => <Ionicons name="cash" {...props} />,
  University: (props: any) => <Ionicons name="business" {...props} />,
  Building2: (props: any) => <Ionicons name="business" {...props} />,

  // Shopping
  ShoppingCart: (props: any) => <Ionicons name="cart" {...props} />,
  ShoppingBag: (props: any) => <Ionicons name="bag" {...props} />,
  Package: (props: any) => <Ionicons name="cube" {...props} />,

  // Location
  MapPin: (props: any) => <Ionicons name="location" {...props} />,
  Navigation: (props: any) => <Ionicons name="navigate" {...props} />,
  Map: (props: any) => <Ionicons name="map" {...props} />,

  // Status
  CheckCircle: (props: any) => <Ionicons name="checkmark-circle" {...props} />,
  XCircle: (props: any) => <Ionicons name="close-circle" {...props} />,
  AlertCircle: (props: any) => <Ionicons name="alert-circle" {...props} />,
  Info: (props: any) => <Ionicons name="information-circle" {...props} />,

  // Technology
  Smartphone: (props: any) => <Ionicons name="phone-portrait" {...props} />,
  Camera: (props: any) => <Ionicons name="camera" {...props} />,
  CameraIcon: (props: any) => <Ionicons name="camera" {...props} />,
  QrCode: (props: any) => <Ionicons name="qr-code" {...props} />,
  Monitor: (props: any) => <Ionicons name="desktop" {...props} />,
  Plug: (props: any) => <Ionicons name="flash" {...props} />,

  // Utilities
  TrendingUp: (props: any) => <Ionicons name="trending-up" {...props} />,
  TrendingDown: (props: any) => <Ionicons name="trending-down" {...props} />,
  Award: (props: any) => <Ionicons name="trophy" {...props} />,
  Zap: (props: any) => <Ionicons name="flash" {...props} />,
  Droplet: (props: any) => <Ionicons name="water" {...props} />,
  Wifi: (props: any) => <Ionicons name="wifi" {...props} />,
  Tv: (props: any) => <Ionicons name="tv" {...props} />,
  Home: (props: any) => <Ionicons name="home" {...props} />,
  Car: (props: any) => <Ionicons name="car" {...props} />,
  Dog: (props: any) => <Ionicons name="paw" {...props} />,
  BookOpen: (props: any) => <Ionicons name="book" {...props} />,
  Palette: (props: any) => <Ionicons name="color-palette" {...props} />,
  Building: (props: any) => <Ionicons name="business" {...props} />,
  Cake: (props: any) => <Ionicons name="restaurant" {...props} />,
  History: (props: any) => <Ionicons name="time" {...props} />,
  Lightbulb: (props: any) => <Ionicons name="bulb" {...props} />,
  Target: (props: any) => <Ionicons name="locate" {...props} />,

  // Food/Utensils
  Utensils: (props: any) => <Ionicons name="restaurant" {...props} />,
  Coffee: (props: any) => <Ionicons name="cafe" {...props} />,

  // Business
  Landmark: (props: any) => <Ionicons name="business" {...props} />,
  Shirt: (props: any) => <Ionicons name="shirt" {...props} />,

  // Additional icons
  UserPlus: (props: any) => <Ionicons name="person-add" {...props} />,
  Calendar: (props: any) => <Ionicons name="calendar" {...props} />,
  Truck: (props: any) => <Ionicons name="car" {...props} />,
  Edit: (props: any) => <Ionicons name="create" {...props} />,
  Store: (props: any) => <Ionicons name="storefront" {...props} />,
  Share: (props: any) => <Ionicons name="share" {...props} />,
  CircleDollarSign: (props: any) => <Ionicons name="cash" {...props} />,
  ShieldCheck: (props: any) => <Ionicons name="shield-checkmark" {...props} />,
  MoreHorizontal: (props: any) => <Ionicons name="ellipsis-horizontal" {...props} />,
  Mail: (props: any) => <Ionicons name="mail" {...props} />,
  ChevronUp: (props: any) => <Ionicons name="chevron-up" {...props} />,
  MessageCircle: (props: any) => <Ionicons name="chatbubble" {...props} />,
  Phone: (props: any) => <Ionicons name="call" {...props} />,
  Minus: (props: any) => <Ionicons name="remove" {...props} />,

  // Additional missing icons found in codebase
  DollarSign: (props: any) => <Ionicons name="cash" {...props} />,
  ChevronLeft: (props: any) => <Ionicons name="chevron-back" {...props} />,

  // Popular icons for future use
  Download: (props: any) => <Ionicons name="download" {...props} />,
  Bookmark: (props: any) => <Ionicons name="bookmark" {...props} />,
  BookmarkOutline: (props: any) => <Ionicons name="bookmark-outline" {...props} />,
  Play: (props: any) => <Ionicons name="play" {...props} />,
  Pause: (props: any) => <Ionicons name="pause" {...props} />,
  VolumeHigh: (props: any) => <Ionicons name="volume-high" {...props} />,
  VolumeMute: (props: any) => <Ionicons name="volume-mute" {...props} />,
  VolumeLow: (props: any) => <Ionicons name="volume-low" {...props} />,
  Fullscreen: (props: any) => <Ionicons name="expand" {...props} />,
  FullscreenExit: (props: any) => <Ionicons name="contract" {...props} />,
  Grid: (props: any) => <Ionicons name="grid" {...props} />,
  List: (props: any) => <Ionicons name="list" {...props} />,
  Options: (props: any) => <Ionicons name="ellipsis-vertical" {...props} />,
  Close: (props: any) => <Ionicons name="close" {...props} />,
  Checkmark: (props: any) => <Ionicons name="checkmark" {...props} />,
  Copy: (props: any) => <Ionicons name="copy" {...props} />,
  Cut: (props: any) => <Ionicons name="cut" {...props} />,
  Paste: (props: any) => <Ionicons name="clipboard" {...props} />,
  Undo: (props: any) => <Ionicons name="arrow-undo" {...props} />,
  Redo: (props: any) => <Ionicons name="arrow-redo" {...props} />,
  Save: (props: any) => <Ionicons name="save" {...props} />,
  Print: (props: any) => <Ionicons name="print" {...props} />,
  Scan: (props: any) => <Ionicons name="scan" {...props} />,
  Barcode: (props: any) => <Ionicons name="barcode" {...props} />,
  Flash: (props: any) => <Ionicons name="flash" {...props} />,
  FlashOff: (props: any) => <Ionicons name="flash-off" {...props} />,
  Image: (props: any) => <Ionicons name="image" {...props} />,
  Video: (props: any) => <Ionicons name="videocam" {...props} />,
  Microphone: (props: any) => <Ionicons name="mic" {...props} />,
  MicrophoneOff: (props: any) => <Ionicons name="mic-off" {...props} />,
  Headset: (props: any) => <Ionicons name="headset" {...props} />,
  Speaker: (props: any) => <Ionicons name="volume-high" {...props} />,
  SpeakerOff: (props: any) => <Ionicons name="volume-mute" {...props} />,
  Bluetooth: (props: any) => <Ionicons name="bluetooth" {...props} />,
  Cellular: (props: any) => <Ionicons name="cellular" {...props} />,
  Battery: (props: any) => <Ionicons name="battery-full" {...props} />,
  BatteryCharging: (props: any) => <Ionicons name="battery-charging" {...props} />,
  BatteryLow: (props: any) => <Ionicons name="battery-low" {...props} />,
  BatteryDead: (props: any) => <Ionicons name="battery-dead" {...props} />,
  LocationOff: (props: any) => <Ionicons name="location-off" {...props} />,
  Compass: (props: any) => <Ionicons name="compass" {...props} />,
  Globe: (props: any) => <Ionicons name="globe" {...props} />,
  Language: (props: any) => <Ionicons name="language" {...props} />,
  Translate: (props: any) => <Ionicons name="language" {...props} />,
  Flag: (props: any) => <Ionicons name="flag" {...props} />,
  Alarm: (props: any) => <Ionicons name="alarm" {...props} />,
  Timer: (props: any) => <Ionicons name="timer" {...props} />,
  Stopwatch: (props: any) => <Ionicons name="stopwatch" {...props} />,
  Calculator: (props: any) => <Ionicons name="calculator" {...props} />,
  CalculatorOutline: (props: any) => <Ionicons name="calculator-outline" {...props} />,

  // Default fallback
  default: (props: any) => <Ionicons name="help-circle" {...props} />,
}

export default Icons
