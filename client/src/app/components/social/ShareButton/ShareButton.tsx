import React, { useState } from 'react';
import { TouchableOpacity, Text, Share, ActivityIndicator } from 'react-native';
import { Ionicons } from '@expo/vector-icons';

export type ShareButtonProps = {
  title?: string;
  message?: string;
  url?: string;
  small?: boolean;
};

const ShareButton: React.FC<ShareButtonProps> = ({ title = 'Share', message, url, small = false }) => {
  const [loading, setLoading] = useState(false);

  const handleShare = async () => {
    if (loading) return;
    setLoading(true);
    try {
      await Share.share({
        title,
        message: message || url || '',
        url,
      });
    } finally {
      setLoading(false);
    }
  };

  const size = small ? 18 : 22;

  return (
    <TouchableOpacity onPress={handleShare} activeOpacity={0.7} disabled={loading}>
      {loading ? (
        <ActivityIndicator size="small" color={'#6b7280'} />
      ) : (
        <Ionicons name={'share-social-outline'} size={size} color={'#6b7280'} />
      )}
    </TouchableOpacity>
  );
};

export default ShareButton;