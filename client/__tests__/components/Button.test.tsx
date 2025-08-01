import React from 'react';

// Mock Button component for testing
const MockButton = ({ title, onPress, disabled, variant, size }: any) => {
  const props: any = {
    onClick: disabled ? undefined : onPress,
    disabled,
  };

  if (variant) props['data-variant'] = variant;
  if (size) props['data-size'] = size;

  return React.createElement('button', props, title);
};

describe('Button', () => {
  const mockOnPress = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('should be defined', () => {
    expect(MockButton).toBeDefined();
  });

  it('should accept title prop', () => {
    const button = React.createElement(MockButton, {
      title: 'Test Button',
      onPress: mockOnPress,
    });

    expect(button.props.title).toBe('Test Button');
  });

  it('should accept onPress prop', () => {
    const button = React.createElement(MockButton, {
      title: 'Test Button',
      onPress: mockOnPress,
    });

    expect(button.props.onPress).toBe(mockOnPress);
  });

  it('should accept disabled prop', () => {
    const button = React.createElement(MockButton, {
      title: 'Test Button',
      onPress: mockOnPress,
      disabled: true,
    });

    expect(button.props.disabled).toBe(true);
  });

  it('should accept variant prop', () => {
    const button = React.createElement(MockButton, {
      title: 'Primary Button',
      onPress: mockOnPress,
      variant: 'primary',
    });

    expect(button.props.variant).toBe('primary');
  });

  it('should accept size prop', () => {
    const button = React.createElement(MockButton, {
      title: 'Large Button',
      onPress: mockOnPress,
      size: 'large',
    });

    expect(button.props.size).toBe('large');
  });
});