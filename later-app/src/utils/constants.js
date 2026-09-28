export const COLORS = {
  background: '#F4F1DE',
  card: '#FFFFFF',
  primary: '#8B0000',
  primaryLight: '#A00000',
  primaryDark: '#6B0000',
  text: '#3D405B',
  subText: '#5A5D7A',
  progress: '#81B29A',
  progressLight: '#95C9AB',
  white: '#FFFFFF',
  offWhite: '#FAFAFA',
  lightGray: '#E5E5E5',
  lighterGray: '#F0F0F0',
  border: 'rgba(139, 0, 0, 0.1)',
  red: '#E63946',
  black: '#000000',
};

export const DARK_COLORS = {
  background: '#121212',
  card: '#1E1E1E',
  primary: '#C53030', // Vibrant contrast wine/red
  primaryLight: '#E53E3E',
  primaryDark: '#8B0000',
  text: '#FFFFFF',
  subText: '#B0B0B0',
  progress: '#81B29A',
  progressLight: '#95C9AB',
  white: '#1E1E1E',
  offWhite: '#252525',
  lightGray: '#333333',
  lighterGray: '#2A2A2A',
  border: 'rgba(255, 255, 255, 0.15)',
  red: '#FF4D4D',
  black: '#000000',
};

export const SPACING = {
  xs: 4,
  sm: 8,
  md: 16,
  lg: 24,
  xl: 32,
  xxl: 48,
  xxxl: 64,
};

export const BORDER_RADIUS = {
  card: 16,
  button: 12,
  input: 12,
  modal: 20,
  progress: 10,
};

export const FONTS = {
  heading: { fontSize: 32, fontWeight: '800', letterSpacing: -1 },
  subheading: { fontSize: 22, fontWeight: '700', letterSpacing: -0.5 },
  body: { fontSize: 18, fontWeight: '400', letterSpacing: 0 },
  small: { fontSize: 16, fontWeight: '500', letterSpacing: 0.2 },
};

export const SHADOWS = {
  card: {
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.15,
    shadowRadius: 12,
    elevation: 8,
  },
  button: {
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.2,
    shadowRadius: 8,
    elevation: 6,
  },
  intervention: {
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 6 },
    shadowOpacity: 0.25,
    shadowRadius: 16,
    elevation: 12,
  },
  modal: {
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 8 },
    shadowOpacity: 0.3,
    shadowRadius: 24,
    elevation: 20,
  },
};

export const GRADIENTS = {
  primary: ['#8B0000', '#A00000'],
  primaryDark: ['#8B0000', '#6B0000'],
  progress: ['#81B29A', '#95C9AB'],
  card: ['#FFFFFF', '#F9F9F9'],
  cardDark: ['#1E1E1E', '#252525'],
  background: ['#F4F1DE', '#FFFFFF'],
  darkScreen: ['#000000', '#1A1A1A'],
  track: ['#E5E5E5', '#F0F0F0'],
};
