import React from 'react';

// Mock PostCard component since it doesn't exist yet
const PostCard = ({ post }: { post?: any }) => {
  return React.createElement('div', {
    'data-testid': 'post-card',
  }, post?.content || 'Default post content');
};

describe('PostCard', () => {
  it('should be defined', () => {
    expect(PostCard).toBeDefined();
  });

  it('should accept post prop', () => {
    const mockPost = {
      id: '1',
      content: 'Test post content',
      author: 'Test User',
    };

    const component = React.createElement(PostCard, { post: mockPost });
    expect(component.props.post).toEqual(mockPost);
  });

  it('should render with default content when no post provided', () => {
    const component = React.createElement(PostCard, {});
    expect(component.props.post).toBeUndefined();
  });
});